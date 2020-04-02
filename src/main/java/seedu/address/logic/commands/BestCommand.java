package seedu.address.logic.commands;

import static seedu.address.commons.util.ModelUtil.validateFinalisation;

import java.util.Comparator;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.BestParameter;
import seedu.address.model.hirelah.Interviewee;
import seedu.address.model.hirelah.IntervieweeToScore;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.storage.Storage;

/**
 * BestCommand describes the behavior of the command
 * that sorts the interviewees based on a certain parameter.
 */
public class BestCommand extends Command {
    public static final String COMMAND_WORD = "best";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_SIZE_NOT_A_NUMBER = "The size of the interviewees provided is not a number.";
    public static final String MESSAGE_NON_POSITIVE_SIZE = "The size of the interviewees provided must be positive.";
    public static final String MESSAGE_PARAM_NOT_FOUND = "The parameter provided: %s is not found.";
    public static final String MESSAGE_SUCCESS = "Here are the best %s interviewees.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + "<number of interviewees> "
            + "[-a <attribute>] [-m <metrics>]"
            + ": Finds best N candidates from the list.\n"
            + "e.g. best 3 -a leadership";

    private final String numberOfInterviewees;
    private final String paramPrefix;
    private final BestParameter paramType;

    public BestCommand(String numberOfInterviewees, BestParameter paramType) {
        this.numberOfInterviewees = numberOfInterviewees;
        this.paramPrefix = "";
        this.paramType = paramType;
    }

    public BestCommand(String numberOfInterviewees, String paramPrefix, BestParameter paramType) {
        this.numberOfInterviewees = numberOfInterviewees;
        this.paramPrefix = paramPrefix;
        this.paramType = paramType;
    }

    /**
     * Executes the best command.
     *
     * @param model {@code Model} which the command should operate on.
     * @param storage
     * @return The corresponding CommandResult instance.
     * @throws CommandException If there is an invalid parameter entered by the client.
     */
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        int size = parseNumberOfInterviewees(numberOfInterviewees);
        Comparator<Interviewee> comparator;
        try {
            switch (paramType) {
            case METRIC:
                comparator = getComparatorByMetric(model.getMetricList(), paramPrefix);
                break;
            case ATTRIBUTE:
                comparator = getComparatorByAttribute(model.getAttributeList(), paramPrefix);
                break;
            default:
                comparator = getOverallComparator(model.getAttributeList());
            }
            getBestN(comparator, size, model);
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_PARAM_NOT_FOUND, paramPrefix));
        }

        return new ToggleCommandResult(
                String.format(MESSAGE_SUCCESS, numberOfInterviewees), ToggleView.BEST_INTERVIEWEE);
    }

    /**
     * Fills the list of best interviewees with the top N interviewees using the given comparator.
     * The interviewees shown could be more than N if ties.
     *
     * @param comparator the comparator to compare interviewees.
     * @param size the number of interviewees to fill into bestNInterviewees.
     * @param model The model of the app.
     */
    private void getBestN(Comparator<Interviewee> comparator, int size, Model model) throws IllegalValueException {
        ObservableList<Interviewee> observableInterviewees = model.getFilteredIntervieweeListView();
        ObservableList<IntervieweeToScore> bestNInterviewees = model.getBestNInterviewees();
        bestNInterviewees.clear();
        FilteredList<Interviewee> filtered = new FilteredList<>(observableInterviewees, Interviewee::isInterviewed);
        SortedList<Interviewee> sorted = new SortedList<>(filtered, comparator);
        int n = Math.min(size, sorted.size());
        for (int i = 0; i < n; i++) {
            bestNInterviewees.add(new IntervieweeToScore(sorted.get(i), getScore(sorted.get(i), model)));
        }

        Interviewee lastBest = bestNInterviewees.get(n - 1).getInterviewee();
        while (bestNInterviewees.size() < sorted.size()
                && comparator.compare(sorted.get(bestNInterviewees.size()), lastBest) == 0) {
            lastBest = sorted.get(bestNInterviewees.size());
            bestNInterviewees.add(new IntervieweeToScore(lastBest, getScore(lastBest, model)));
        }
    }

    private double getScore(Interviewee interviewee, Model model) throws IllegalValueException {
        switch (paramType) {
        case METRIC:
            return computeByMetric(interviewee, model.getMetricList(), paramPrefix);
        case ATTRIBUTE:
            return computeByAttribute(interviewee, model.getAttributeList(), paramPrefix);
        default:
            return getOverallScore(model.getAttributeList(), interviewee);
        }
    }

    /**
     * Computes the score of an interviewee based on the Metric provided.
     *
     * @param interviewee The interviewee that his score wants to be computed.
     * @param metrics The metric list.
     * @param paramPrefix The prefix of the metric name.
     * @return The corresponding score
     * @throws IllegalValueException If the prefix of the metric is not found or there are multiple metrics with the
     * same prefix.
     */
    private double computeByMetric(Interviewee interviewee, MetricList metrics, String paramPrefix)
            throws IllegalValueException {
        return metrics.find(paramPrefix).computeScore(interviewee);
    }

    /**
     * Computes the score of an interviewee based on the Metric provided.
     *
     * @param interviewee The interviewee that his score wants to be computed.
     * @param attributes The attribute list.
     * @param paramPrefix The prefix of the attribute name.
     * @return The corresponding score.
     * @throws IllegalValueException If the prefix of the attribute is not found or there are multiple attributes with
     * the same prefix.
     */
    private double computeByAttribute(Interviewee interviewee,
                                                       AttributeList attributes, String paramPrefix)
            throws IllegalValueException {
        return interviewee.getScore(attributes.find(paramPrefix));
    }

    /**
     * Gets the Interviewee Comparator based on the Metric provided.
     *
     * @param metrics The metric list.
     * @param paramPrefix The prefix of the metric name.
     * @return The corresponding comparator.
     * @throws IllegalValueException If the prefix of the metric is not found or there are multiple metrics with the
     * same prefix.
     */
    private Comparator<Interviewee> getComparatorByMetric(MetricList metrics, String paramPrefix)
            throws IllegalValueException {
        return metrics.find(paramPrefix).getComparator();
    }

    /**
     * Gets the Interviewee Comparator based on the Attribute provided.
     *
     * @param attributes The attribute list.
     * @param paramPrefix The prefix of the attribute name.
     * @return The corresponding comparator.
     * @throws IllegalValueException If the prefix of the attribute is not found or there are multiple attributes with
     * the same prefix.
     */
    private Comparator<Interviewee> getComparatorByAttribute(AttributeList attributes, String paramPrefix)
            throws IllegalValueException {
        Attribute attribute = attributes.find(paramPrefix);
        return (interviewee, anotherInterviewee) -> {
            double totalScoreFirst = interviewee.getScore(attribute);
            double totalScoreSecond = anotherInterviewee.getScore(attribute);
            return totalScoreSecond - totalScoreFirst < 0 ? -1 : totalScoreFirst == totalScoreSecond ? 0 : 1;
        };
    }

    /**
     * Gets the Interviewee Comparator based on their overall performances.
     *
     * @param attributes The attribute list.
     * @return The corresponding comparator.
     */
    private Comparator<Interviewee> getOverallComparator(AttributeList attributes) {
        return (interviewee, anotherInterviewee) -> {
            double totalScoreFirst = getOverallScore(attributes, interviewee);
            double totalScoreSecond = getOverallScore(attributes, anotherInterviewee);
            return totalScoreSecond - totalScoreFirst < 0 ? -1 : totalScoreFirst == totalScoreSecond ? 0 : 1;
        };
    }

    /**
     * Computes the total overall score of an interviewee.
     *
     * @param attributes The attribute list.
     * @param interviewee The interviewee.
     * @return The overall score of the interviewee.
     */
    private double getOverallScore(AttributeList attributes, Interviewee interviewee) {
        ObservableList<Attribute> observableList = attributes.getObservableList();

        double totalScore = 0;
        for (Attribute attribute: observableList) {
            totalScore += interviewee.getScore(attribute);
        }
        return totalScore;
    }

    /**
     * Parses the number of interviewees entered by the client to the corresponding integer.
     *
     * @param numberOfInterviewees The String representing the size of the interviewees entered by the client.
     * @return The corresponding integer value.
     * @throws CommandException If the value entered is not a number or the number is less than or equal to zero.
     */
    private int parseNumberOfInterviewees(String numberOfInterviewees) throws CommandException {
        try {
            int result = Integer.parseInt(numberOfInterviewees);
            if (result <= 0) {
                throw new CommandException(MESSAGE_NON_POSITIVE_SIZE);
            }

            return result;
        } catch (NumberFormatException e) {
            throw new CommandException(MESSAGE_SIZE_NOT_A_NUMBER);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BestCommand // instanceof handles nulls
                && numberOfInterviewees.equals(((BestCommand) other).numberOfInterviewees)
                && paramType.equals(((BestCommand) other).paramType));
    }
}
