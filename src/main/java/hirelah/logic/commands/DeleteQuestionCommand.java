package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.commons.util.ModelUtil;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.QuestionList;
import hirelah.storage.Storage;

/**
 * DeleteQuestionCommand describes the behavior when the
 * client wants to delete a question from the list.
 */

public class DeleteQuestionCommand extends Command {
    public static final String COMMAND_WORD = "question";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_FORMAT = "delete " + COMMAND_WORD + " <question number>";
    public static final String MESSAGE_FUNCTION =
            ": Deletes the question identified by its index number in the question list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: delete " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_QUESTION_SUCCESS = "Deleted question: %1$s";

    private final int questionIndex;

    public DeleteQuestionCommand(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        ModelUtil.validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        QuestionList questions = model.getQuestionList();
        try {
            return new ToggleCommandResult(String.format(MESSAGE_DELETE_QUESTION_SUCCESS,
                    questions.delete(questionIndex)),
                    ToggleView.QUESTION);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteQuestionCommand // instanceof handles nulls
                && questionIndex == (((DeleteQuestionCommand) other).questionIndex)); // state check
    }
}
