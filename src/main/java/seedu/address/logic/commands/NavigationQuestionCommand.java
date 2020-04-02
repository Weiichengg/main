package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.exceptions.IllegalActionException;
import seedu.address.model.hirelah.storage.Storage;

/**
 * NavigationQuestionCommand describes the behavior when the
 * client wants to navigate to a certain remark of the interview.
 */
public class NavigationQuestionCommand extends Command {
    public static final String COMMAND_WORD = "goto";
    public static final String MESSAGE_NAVIGATION_QUESTION_SUCCESS = "Here is the remark of question %d!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Navigate to a particular answer of a question from an interviewee.\n"
            + "Parameters: questionNumber\n"
            + "Example:  " + COMMAND_WORD + " q10";

    private final int questionIndex;

    public NavigationQuestionCommand(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        if (!model.hasCurrentInterviewee()) {
            throw new CommandException("You need to open a transcript of a particular interviewee "
                    + "to go to the answer of a question.");
        }
        try {
            int remarkIndex = model.getCurrentTranscript().getIndexOfQuestion(questionIndex);
            return new NavigationCommandResult(String.format(MESSAGE_NAVIGATION_QUESTION_SUCCESS,
                    questionIndex), remarkIndex);
        } catch (IllegalValueException | IllegalActionException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NavigationQuestionCommand
                && questionIndex == ((NavigationQuestionCommand) other).questionIndex);
    }
}
