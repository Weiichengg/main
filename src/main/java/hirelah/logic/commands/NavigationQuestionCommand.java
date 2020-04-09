package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * NavigationQuestionCommand describes the behavior when the
 * client wants to navigate to a certain remark of the interview.
 */
public class NavigationQuestionCommand extends Command {

    public static final String COMMAND_WORD = "q";
    public static final String MESSAGE_FORMAT = "goto " + COMMAND_WORD + "<questionNumber>";
    public static final String MESSAGE_FUNCTION =
            ": Navigate to a particular answer of a question from an interviewee.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Parameters: questionNumber\n"
            + "Example:  " + COMMAND_WORD + " q10";

    public static final String MESSAGE_NAVIGATION_QUESTION_SUCCESS = "Here is the remark of question %d!";

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
