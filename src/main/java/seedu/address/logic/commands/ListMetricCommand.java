package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * ListMetricCommand describes the behavior when the
 * client wants to list the metrics available.
 */
public class ListMetricCommand extends Command {
    public static final String COMMAND_WORD = "metric";
    public static final String MESSAGE_SUCCESS = "Here is the list of metrics:";
    public static final String MESSAGE_FORMAT = "list " + COMMAND_WORD;
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": List the metrics from the Metric list.\n"
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
