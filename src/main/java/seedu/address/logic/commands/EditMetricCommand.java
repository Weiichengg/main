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
 * EditMetricCommand describes the behavior of HireLah!
 * when a user wants to edit a metric.
 */
public class EditMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_FORMAT = "edit " + COMMAND_WORD + "<metric> [-n <metric name>] [-a <attribute>"
            + " -w <score>]...";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": Edits the metric.\n"
            + "Example: edit " + COMMAND_WORD + " extremeLeadership -n extremeDictatorship -a leadership -w 100";

    public static final String MESSAGE_EDIT_METRIC_SUCCESS = "Edited metric: %s";

    private final String toEdit;
    private final String updatedName;
    private final List<String> attributePrefixes;
    private final List<Double> weightages;

    public EditMetricCommand(String toEdit, String updatedName, List<String> attributePrefixes,
                             List<Double> weightages) {
        this.toEdit = toEdit;
        this.updatedName = updatedName;
        this.attributePrefixes = attributePrefixes;
        this.weightages = weightages;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        MetricList metrics = model.getMetricList();
        AttributeList attributes = model.getAttributeList();

        try {
            metrics.edit(toEdit, updatedName, attributes, attributePrefixes, weightages);
            return new ToggleCommandResult(
                    String.format(MESSAGE_EDIT_METRIC_SUCCESS, this.toEdit), ToggleView.METRIC);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }
}
