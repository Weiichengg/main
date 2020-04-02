package seedu.address.logic.commands.interview;

import java.io.IOException;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * Remark command is an Interview phase command that adds a Remark at the current interview time
 * to the Interviewee's Transcript.
 */
public class RemarkCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Added the remark.";

    private final String remark;

    public RemarkCommand(String remark) {
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        model.getCurrentTranscript().addRemark(this.remark);
        try {
            storage.saveTranscript(model.getCurrentInterviewee());
        } catch (IOException e) {
            throw new CommandException("Error occurred while saving data!");
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
