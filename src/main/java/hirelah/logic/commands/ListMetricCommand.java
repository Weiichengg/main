package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * ListMetricCommand describes the behavior when the
 * client wants to list the metrics available.
 */
public class ListMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final String MESSAGE_SUCCESS = "Here is the list of metrics:";
    public static final String MESSAGE_FORMAT = "list " + COMMAND_WORD;
    public static final String MESSAGE_FUNCTION = ": List the metrics from the Metric list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: list " + COMMAND_WORD;

    /**
     * Creates a ListMetricCommand to list the {@code Metric}
     */
    public ListMetricCommand() {

    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        return new ToggleCommandResult(MESSAGE_SUCCESS, ToggleView.METRIC);
    }
}
