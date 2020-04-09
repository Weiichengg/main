package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.commons.util.ModelUtil;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.AttributeList;
import hirelah.storage.Storage;

/**
 * DeleteAttributeCommand describes the behavior when the
 * client wants to delete an attribute from the list.
 */
public class DeleteAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_FORMAT = "delete " + COMMAND_WORD + " <attribute>";
    public static final String MESSAGE_FUNCTION = ": Deletes the attribute identified by its name.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: delete " + COMMAND_WORD + " leadership";

    public static final String MESSAGE_DELETE_ATTRIBUTE_SUCCESS = "Deleted attribute: %1$s";

    private final String attributePrefix;

    public DeleteAttributeCommand(String attributePrefix) {
        this.attributePrefix = attributePrefix;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        ModelUtil.validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        AttributeList attributes = model.getAttributeList();
        try {
            Attribute attribute = attributes.delete(attributePrefix);
            return new ToggleCommandResult(String.format(MESSAGE_DELETE_ATTRIBUTE_SUCCESS, attribute),
                    ToggleView.ATTRIBUTE);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteAttributeCommand // instanceof handles nulls
                && attributePrefix.equals(((DeleteAttributeCommand) other).attributePrefix)); // state check
    }
}
