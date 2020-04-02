package seedu.address.logic.commands.interview;

import java.io.IOException;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ToggleCommandResult;
import seedu.address.logic.commands.ToggleView;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.Transcript;
import seedu.address.model.hirelah.storage.Storage;

/**
 * Ends the current interview and returns to the Interviewee page. Will throw an error if the interviewee still
 * has missing attributes without a score.
 */
public class EndCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Ended interview with %s.";
    public static final String MESSAGE_SCORELESS_ATTRIBUTES =
            "Unable to finish interview with some attributes not scored yet!";

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        Transcript transcript = model.getCurrentTranscript();
        for (Attribute attribute : model.getAttributeList()) {
            if (!transcript.isAttributeScored(attribute)) {
                throw new CommandException(MESSAGE_SCORELESS_ATTRIBUTES);
            }
        }
        transcript.complete();
        CommandResult result = new ToggleCommandResult(
                String.format(MESSAGE_SUCCESS, model.getCurrentInterviewee()),
                ToggleView.INTERVIEWEE);
        try {
            storage.saveTranscript(model.getCurrentInterviewee());
        } catch (IOException e) {
            throw new CommandException("Error occurred while saving data!");
        }
        model.endInterview();
        return result;
    }
}
