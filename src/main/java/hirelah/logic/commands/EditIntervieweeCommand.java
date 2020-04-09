package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * EditIntervieweeCommand describes the behavior when the
 * client wants to update an intervewee's name from the list.
 */

public class EditIntervieweeCommand extends Command {
    public static final String COMMAND_WORD = "interviewee";
    public static final String MESSAGE_FORMAT = "edit " + COMMAND_WORD + " <interviewee> [-n <new name>] "
            + "[-aka <new alias>]";
    public static final String MESSAGE_FUNCTION = ": Edits the interviewee's name and/or alias.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: edit " + COMMAND_WORD + " Doe -n Janice Doe -aka JDoe";

    public static final String MESSAGE_EDIT_INTERVIEWEE_SUCCESS = "Edited interviewee: %1$s";

    private final String identifier;
    private final String updatedName;
    private final String updatedAlias;

    public EditIntervieweeCommand(String identifier, String updatedName, String updatedAlias) {
        this.identifier = identifier;
        this.updatedName = updatedName;
        this.updatedAlias = updatedAlias;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        IntervieweeList interviewees = model.getIntervieweeList();
        try {
            if (updatedName.equals("")) {
                interviewees.setAlias(identifier, updatedAlias);
            } else if (updatedAlias.equals("")) {
                interviewees.setName(identifier, updatedName);
            } else {
                interviewees.setNameAndAlias(identifier, updatedName, updatedAlias);
            }

        } catch (IllegalActionException | IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
        return new ToggleCommandResult(String.format(MESSAGE_EDIT_INTERVIEWEE_SUCCESS, identifier),
                ToggleView.INTERVIEWEE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditIntervieweeCommand // instanceof handles nulls
                && identifier.equals(((EditIntervieweeCommand) other).identifier)
                && updatedName.equals(((EditIntervieweeCommand) other).updatedName)
                && updatedAlias.equals(((EditIntervieweeCommand) other).updatedAlias)); // state check
    }

}
