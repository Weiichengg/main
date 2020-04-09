package hirelah.logic.commands;

import static hirelah.commons.util.ModelUtil.validateFinalisation;
import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Metric;
import hirelah.model.hirelah.MetricList;
import hirelah.storage.Storage;

/**
 * DeleteMetricCommand describes the behavior when the
 * client wants to delete an attribute from the list.
 */

public class DeleteMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = true;
    public static final String MESSAGE_FORMAT = "delete " + COMMAND_WORD + "<metric>";
    public static final String MESSAGE_FUNCTION = ": Deletes the metric identified by its name.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
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
