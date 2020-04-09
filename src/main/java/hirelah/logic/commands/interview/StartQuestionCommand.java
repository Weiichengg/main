package hirelah.logic.commands.interview;

import java.io.IOException;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionList;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * Marks the beginning of the answer to a question in the Interview transcript.
 */
public class StartQuestionCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Marked the start of question %d";

    private final int questionNumber;

    public StartQuestionCommand(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        QuestionList questionList = model.getQuestionList();
        try {
            Question question = questionList.find(questionNumber);
            model.getCurrentTranscript().startQuestion(questionNumber, question);
        } catch (IllegalValueException | IllegalActionException e) {
            throw new CommandException(e.getMessage());
        }
        try {
            storage.saveTranscript(model.getCurrentInterviewee());
        } catch (IOException e) {
            throw new CommandException("Error while saving data!");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, questionNumber));
    }
}
