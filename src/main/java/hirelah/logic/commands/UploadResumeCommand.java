package hirelah.logic.commands;

import java.io.File;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.logic.util.CommandUtil;
import hirelah.model.Model;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;
import javafx.stage.FileChooser;

/**
 * Adds a pdf resume file to the given Interviewee.
 */
public class UploadResumeCommand extends Command {
    public static final String COMMAND_WORD = "upload";
    public static final String MESSAGE_SUCCESS = "Successfully added the resume!";
    public static final String MESSAGE_FILE_NOT_FOUND = "Could not find the file!";
    public static final String MESSAGE_FORMAT = COMMAND_WORD + " <interviewee> [-p <path>]";
    public static final String MESSAGE_FUNCTION = ": Uploads a resume to a particular interviewee from the specified "
            + "path if provided, open a file picker otherwise.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example:" + COMMAND_WORD
            + " Jane Doe ";

    private String path;
    private String identifier;

    public UploadResumeCommand(String identifier, String path) {
        this.path = path;
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
        File resume;
        if (path == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a pdf file");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            resume = fileChooser.showOpenDialog(null);
        } else {
            resume = new File(path);
        }
        if (resume == null || !resume.exists()) {
            throw new CommandException(MESSAGE_FILE_NOT_FOUND);
        }
        interviewee.setResume(resume);
        CommandUtil.saveInterviewees(model, storage);
        return new ToggleCommandResult(MESSAGE_SUCCESS, ToggleView.INTERVIEWEE);
    }

    @Override
    public boolean equals(Object other) {
        if (this.path == null) {
            return other == this // short circuit if same object
                    || (other instanceof UploadResumeCommand // instanceof handles nulls
                    && this.identifier.equals(((UploadResumeCommand) other).identifier));
        }
        return other == this // short circuit if same object
                || (other instanceof UploadResumeCommand // instanceof handles nulls
                && this.path.equals(((UploadResumeCommand) other).path)
                && this.identifier.equals(((UploadResumeCommand) other).identifier));
    }
}
