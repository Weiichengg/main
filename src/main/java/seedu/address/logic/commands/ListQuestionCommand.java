package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * ListQuestionCommand describes the behavior when the
 * client wants to list the questions.
 */

public class ListQuestionCommand extends Command {
    public static final String COMMAND_WORD = "question";
    public static final String MESSAGE_SUCCESS = "Here is the list of questions:";
    public static final String MESSAGE_FORMAT = "list " + COMMAND_WORD;
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + ": List the questions from the Question list.\n"
            + "Example: list " + COMMAND_WORD;

    /**
     * Creates a ListQuestionCommand to list the {@code Question}
     */
    public ListQuestionCommand() {

    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        return new ToggleCommandResult(MESSAGE_SUCCESS, ToggleView.QUESTION);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListQuestionCommand);
    }
}
