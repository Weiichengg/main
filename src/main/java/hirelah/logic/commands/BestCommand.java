package hirelah.logic.commands;

import static hirelah.commons.util.ModelUtil.validateFinalisation;

import java.util.Comparator;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.BestParameter;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeToScore;
import hirelah.model.hirelah.MetricList;
import hirelah.storage.Storage;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * BestCommand describes the behavior of the command
 * that sorts the interviewees based on a certain parameter.
 */
public class BestCommand extends Command {
    public static final String COMMAND_WORD = "best";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_PARAM_NOT_FOUND = "The parameter provided: %s is not found.";
    public static final String MESSAGE_NO_INTERVIEWED_INTERVIEWEE = "There is no interviewee that has been interviewed";
    public static final String MESSAGE_SUCCESS = "Here are the best %s interviewees.";
    public static final String MESSAGE_SUCCESS_LACK_INTERVIEWEE = "There are only %s interviewed interviewees";
    public static final String MESSAGE_SUCCESS_WITH_TIE = "There are ties. Here are the best %s interviewees";
    public static final String MESSAGE_FORMAT = COMMAND_WORD + "<number of interviewees> "
            + "[-a <attribute>] [-m <metrics>]";
    public static final String MESSAGE_FUNCTION = ": Finds best candidates from the list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "e.g. interviewee -best 3 -a leadership";

    private final int numberOfInterviewees;
    private final String paramPrefix;
    private final BestParameter paramType;

    public BestCommand(int numberOfInterviewees, BestParameter paramType) {
        this.numberOfInterviewees = numberOfInterviewees;
        this.paramPrefix = "";
        this.paramType = paramType;
    }

    public BestCommand(int numberOfInterviewees, String paramPrefix, BestParameter paramType) {
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
            int finalSize = getBest(comparator, numberOfInterviewees, model);

            if (finalSize < numberOfInterviewees) {
                return new ToggleCommandResult(
                        String.format(MESSAGE_SUCCESS_LACK_INTERVIEWEE, finalSize), ToggleView.BEST_INTERVIEWEE);
            } else if (finalSize > numberOfInterviewees) {
                return new ToggleCommandResult(
                        String.format(MESSAGE_SUCCESS_WITH_TIE, finalSize), ToggleView.BEST_INTERVIEWEE);
            }
            return new ToggleCommandResult(
                    String.format(MESSAGE_SUCCESS, numberOfInterviewees), ToggleView.BEST_INTERVIEWEE);

        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_PARAM_NOT_FOUND, paramPrefix));
        }


    }

    /**
     * Fills the list of best interviewees with the top N interviewees using the given comparator.
     * The interviewees shown could be more than N if ties.
     *
     * @param comparator the comparator to compare interviewees.
     * @param size the number of interviewees to fill into bestNInterviewees.
     * @param model The model of the app.
     * @return The size of the returned inter
     */
    private int getBest(Comparator<Interviewee> comparator, int size, Model model) throws IllegalValueException,
            CommandException {
        ObservableList<Interviewee> observableInterviewees = model.getIntervieweeListView();
        ObservableList<IntervieweeToScore> bestNInterviewees = model.getBestNInterviewees();
        bestNInterviewees.clear();
        FilteredList<Interviewee> filtered = new FilteredList<>(observableInterviewees, Interviewee::isInterviewed);
        SortedList<Interviewee> sorted = new SortedList<>(filtered, comparator);
        int n = Math.min(size, sorted.size());

        if (n == 0) {
            throw new CommandException(MESSAGE_NO_INTERVIEWED_INTERVIEWEE);
        }
        for (int i = 0; i < n; i++) {
            bestNInterviewees.add(new IntervieweeToScore(sorted.get(i), getScore(sorted.get(i), model)));
        }

        Interviewee lastBest = bestNInterviewees.get(n - 1).getInterviewee();
        while (bestNInterviewees.size() < sorted.size()
                && comparator.compare(sorted.get(bestNInterviewees.size()), lastBest) == 0) {
            lastBest = sorted.get(bestNInterviewees.size());
            bestNInterviewees.add(new IntervieweeToScore(lastBest, getScore(lastBest, model)));
        }
        return bestNInterviewees.size();
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

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BestCommand // instanceof handles nulls
                && numberOfInterviewees == (((BestCommand) other).numberOfInterviewees)
                && paramType.equals(((BestCommand) other).paramType));
    }
}
