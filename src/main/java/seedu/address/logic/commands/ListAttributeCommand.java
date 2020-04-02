package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * ListAttributeCommand describes the behavior when the
 * client wants to list the attributes.
 */

public class ListAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final String MESSAGE_SUCCESS = "Here is the list of attributes:";
    public static final String MESSAGE_FORMAT = "list " + COMMAND_WORD;
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": List the attribute from the Attribute list.\n"
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
