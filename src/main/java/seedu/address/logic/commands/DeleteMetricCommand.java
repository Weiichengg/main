package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.ModelUtil.validateFinalisation;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.Metric;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.storage.Storage;

/**
 * DeleteMetricCommand describes the behavior when the
 * client wants to delete an attribute from the list.
 */

public class DeleteMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_USAGE = "delete " + COMMAND_WORD + "<metric>"
            + ": Deletes the metric identified by its name.\n"
            + "Example: delete " + COMMAND_WORD + " extremeDictatorship";

    public static final String MESSAGE_DELETE_METRIC_SUCCESS = "Deleted metric: %1$s";

    private final String metricPrefix;

    public DeleteMetricCommand(String metricPrefix) {
        this.metricPrefix = metricPrefix;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);

        MetricList metrics = model.getMetricList();
        try {
            Metric metric = metrics.delete(metricPrefix);
            return new ToggleCommandResult(String.format(MESSAGE_DELETE_METRIC_SUCCESS,
                    metric), ToggleView.METRIC);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteMetricCommand // instanceof handles nulls
                && metricPrefix.equals(((DeleteMetricCommand) other).metricPrefix)); // state check
    }
}
