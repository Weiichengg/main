package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.ModelUtil.validateFinalisation;

import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.storage.Storage;

/**
 * AddMetricCommand describes the behavior of HireLah!
 * when a user wants to delete the metric from the list.
 */

public class AddMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_SUCCESS = "New metric added: %1$s";
    public static final String MESSAGE_FORMAT = "add " + COMMAND_WORD + " <metric> [-a <attribute> -w <score>]...";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": Adds a metric to the Metric list.\n"
            + "Example: new " + COMMAND_WORD + " extremeLeader -a leadership -w 0.7 -a tenacity -w 0.6";


    private final String toAdd;
    private final List<String> attributePrefixes;
    private final List<Double> addedWeights;

    /**
     * Creates an AddMetricCommand to add the specified {@code Metric}
     */

    public AddMetricCommand(String toAdd, List<String> attributePrefixes, List<Double> addedWeights) {
        this.toAdd = toAdd;
        this.attributePrefixes = attributePrefixes;
        this.addedWeights = addedWeights;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        MetricList metrics = model.getMetricList();
        AttributeList attributes = model.getAttributeList();

        try {
            metrics.add(toAdd, attributes, attributePrefixes, addedWeights);
            return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, toAdd), ToggleView.METRIC);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
