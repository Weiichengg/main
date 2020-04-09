package hirelah.logic.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * Opens the resume of the given interviewee, using the System default PDF viewer.
 */
public class OpenResumeCommand extends Command {
    public static final String COMMAND_WORD = "resume";
    public static final String MESSAGE_SUCCESS = "Resume of %s opened.";
    public static final String MESSAGE_NO_RESUME = "No resume uploaded!";
    public static final String MESSAGE_RESUME_NOT_FOUND =
            "Cannot find a resume at %s!\nPlease upload a resume via the upload command";

    private String identifier;

    public OpenResumeCommand(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        Interviewee interviewee;
        try {
            interviewee = model.getIntervieweeList().getInterviewee(identifier);
        } catch (IllegalActionException e) {
            throw new CommandException(e.getMessage());
        }

        File resume = interviewee.getResume().orElseThrow(() -> new CommandException(MESSAGE_NO_RESUME));
        if (!resume.exists()) {
            throw new CommandException(String.format(MESSAGE_RESUME_NOT_FOUND, resume));
        }
        openFile(resume);
        return new CommandResult(String.format(MESSAGE_SUCCESS, identifier));
    }

    /**
     * Opens a file that exists.
     *
     * @param resume a file that is guaranteed to exist.
     * @throws CommandException if Desktop is not supported on the platform the app is running on.
     */
    private void openFile(File resume) throws CommandException {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(resume);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            throw new CommandException("Cannot open pdf files!");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OpenResumeCommand // instanceof handles nulls
                && identifier.equals(((OpenResumeCommand) other).identifier));
    }
}
