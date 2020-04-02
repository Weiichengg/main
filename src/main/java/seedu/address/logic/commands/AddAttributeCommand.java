package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.ModelUtil.validateFinalisation;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.storage.Storage;

/**
 * AddAttributeCommand describes the behavior when the
 * client wants to add an attribute to the list.
 */

public class AddAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_SUCCESS = "New attribute added: %1$s";
    public static final String MESSAGE_FORMAT = "add " + COMMAND_WORD + " <attribute>";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": Adds an attribute to the Attribute list.\n"
            + "Example: add " + COMMAND_WORD + " leadership";

    private final String toAdd;

    /**
     * Creates an AddAttributeCommand to add the specified {@code Attribute}
     */
    public AddAttributeCommand(String attribute) {
        toAdd = attribute;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        AttributeList attributes = model.getAttributeList();

        try {
            attributes.add(toAdd);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }

        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, toAdd), ToggleView.ATTRIBUTE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAttributeCommand // instanceof handles nulls
                && toAdd.equals(((AddAttributeCommand) other).toAdd));
    }
}
