package seedu.address.logic.commands.interview;

import java.io.IOException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.storage.Storage;

/**
 * Attempts to find the attribute specified by the user, and update the score of that attribute for
 * the currently interviewed interviewee.
 */
public class ScoreCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Scored %.2f to %s";

    private String attributePrefix;
    private double score;

    public ScoreCommand(String attributePrefix, double score) {
        this.attributePrefix = attributePrefix;
        this.score = score;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        Attribute attribute;
        try {
            attribute = model.getAttributeList().find(attributePrefix);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
        model.getCurrentTranscript().setAttributeScore(attribute, this.score);
        try {
            storage.saveTranscript(model.getCurrentInterviewee());
        } catch (IOException e) {
            throw new CommandException("Error occurred while saving data!");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.score, attribute));
    }
}
