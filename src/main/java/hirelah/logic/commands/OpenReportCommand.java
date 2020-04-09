package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * OpenReportCommand describes the behavior when the
 * client wants to open an interview report of an interviewee.
 */
public class OpenReportCommand extends Command {
    public static final String COMMAND_WORD = "open";
    public static final String MESSAGE_NOT_INTERVIEWED = "%s has not completed their interview.";
    public static final String MESSAGE_FUNCTION = ": Opens the interview report of an interviewee.\n";
    public static final String MESSAGE_USAGE = COMMAND_WORD + MESSAGE_FUNCTION
            + "Parameters: IDENTIFIER\n"
            + "Example: " + COMMAND_WORD + " John Doe";

    public static final String MESSAGE_SUCCESS = "Successfully opened Interviewee report: %s";

    private final String identifier;

    /**
     * Creates an OpenReportCommand to open
     * the report of an {@code Interviewee}.
     */
    public OpenReportCommand(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        IntervieweeList interviewees = model.getIntervieweeList();
        Interviewee interviewee;
        try {
            interviewee = interviewees.getInterviewee(identifier);
            if (!interviewee.isInterviewed()) {
                throw new CommandException(String.format(MESSAGE_NOT_INTERVIEWED, interviewee));
            }
            model.setCurrentInterviewee(interviewee);
        } catch (IllegalActionException e) {
            throw new CommandException(e.getMessage());
        }
        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, interviewee),
                ToggleView.TRANSCRIPT);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OpenReportCommand // handles null
                && identifier.equals(((OpenReportCommand) other).identifier)); // state check
    }
}
