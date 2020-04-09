package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * ListAttributeCommand describes the behavior when the
 * client wants to list the attributes.
 */

public class ListAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final String MESSAGE_SUCCESS = "Here is the list of attributes:";
    public static final String MESSAGE_FORMAT = "list " + COMMAND_WORD;
    public static final String MESSAGE_FUNCTION = ": List the attribute from the Attribute list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: list " + COMMAND_WORD;

    /**
     * Creates a ListAttributeCommand to list the {@code Attribute}
     */
    public ListAttributeCommand() {

    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        return new ToggleCommandResult(MESSAGE_SUCCESS, ToggleView.ATTRIBUTE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListAttributeCommand);
    }
}
