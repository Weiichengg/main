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
 * EditAttributeCommand describes the behavior when the
 * client wants to update an attribute from the list.
 */

public class EditAttributeCommand extends Command {
    public static final String COMMAND_WORD = "attribute";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_FORMAT = "edit " + COMMAND_WORD + " <old attribute> -a <new attribute>";
    public static final String MESSAGE_FUNCTION = ": Edits the attribute identified by the name.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: edit " + COMMAND_WORD + " leadership -a tenacity";

    public static final String MESSAGE_EDIT_ATTRIBUTE_SUCCESS = "Edited attribute: %s to %s";

    private final String attributePrefix;
    private final String updatedAttribute;

    public EditAttributeCommand(String attributePrefix, String updatedAttribute) {
        this.attributePrefix = attributePrefix;
        this.updatedAttribute = updatedAttribute;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        ModelUtil.validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        AttributeList attributes = model.getAttributeList();
        try {
            Attribute attribute = attributes.edit(attributePrefix, updatedAttribute);
            return new ToggleCommandResult(String.format(MESSAGE_EDIT_ATTRIBUTE_SUCCESS, attribute, updatedAttribute),
                    ToggleView.ATTRIBUTE);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditAttributeCommand // instanceof handles nulls
                && attributePrefix.equals(((EditAttributeCommand) other).attributePrefix)
                && updatedAttribute.equals(((EditAttributeCommand) other).updatedAttribute)); // state check
    }
}
