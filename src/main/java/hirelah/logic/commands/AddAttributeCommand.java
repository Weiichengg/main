package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.commons.util.ModelUtil;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.AttributeList;
import hirelah.storage.Storage;

/**
 * AddAttributeCommand describes the behavior when the
 * client wants to add an attribute to the list.
 */
public class AddAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_SUCCESS = "New attribute added: %1$s";
    public static final String MESSAGE_FORMAT = "add " + COMMAND_WORD + " <attribute>";
    public static final String MESSAGE_FUNCTION = ": Adds an attribute to the Attribute list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
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
        ModelUtil.validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
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
