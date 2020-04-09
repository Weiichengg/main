package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.commons.util.ModelUtil;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.QuestionList;
import hirelah.storage.Storage;

/**
 * AddQuestionCommand describes the behavior when the
 * client wants to add a question to the list.
 */

public class AddQuestionCommand extends Command {
    public static final String COMMAND_WORD = "question";
    public static final boolean DESIRED_MODEL_FINALIZED_STATE = false;
    public static final String MESSAGE_SUCCESS = "New question added: %1$s";
    public static final String MESSAGE_FUNCTION = ": Adds an question to the Question list.\n";
    public static final String MESSAGE_FORMAT = "add " + COMMAND_WORD + "<question description>";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: add " + COMMAND_WORD + " what is this question?";


    private final String toAdd;

    /**
     * Creates an AddQuestionCommand to add the specified {@code Question}
     */
    public AddQuestionCommand(String question) {
        toAdd = question;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        ModelUtil.validateFinalisation(model, DESIRED_MODEL_FINALIZED_STATE);
        QuestionList questions = model.getQuestionList();
        try {
            questions.add(toAdd);
        } catch (IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }

        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, toAdd), ToggleView.QUESTION);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddQuestionCommand // instanceof handles nulls
                && toAdd.equals(((AddQuestionCommand) other).toAdd));
    }
}
