package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * DeleteIntervieweeCommand describes the behavior when the
 * client wants to delete an interviewee from the list.
 */

public class DeleteIntervieweeCommand extends Command {
    public static final String COMMAND_WORD = "interviewee";
    public static final String MESSAGE_SUCCESS = "Deleted interviewee: %1$s";
    public static final String MESSAGE_FORMAT = "delete " + COMMAND_WORD + " <interviewee>";
    public static final String MESSAGE_FUNCTION = ": Deletes an interviewee to the Interviewee list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: delete " + COMMAND_WORD + " Doe";

    private final String identifier;

    /**
     * Creates an DeleteIntervieweeCommand to delete the specified {@code Interviewee}
     */
    public DeleteIntervieweeCommand(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        IntervieweeList interviewees = model.getIntervieweeList();

        try {
            interviewees.deleteInterviewee(identifier);
        } catch (IllegalActionException e) {
            throw new CommandException(e.getMessage());
        }

        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, identifier), ToggleView.INTERVIEWEE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteIntervieweeCommand // instanceof handles nulls
                && identifier.equals(((DeleteIntervieweeCommand) other).identifier));
    }
}
