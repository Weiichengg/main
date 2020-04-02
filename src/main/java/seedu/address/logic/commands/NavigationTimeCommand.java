package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * NavigationTimeCommand describes the behavior when the
 * client wants to navigate to a certain remark of the interview.
 */
public class NavigationTimeCommand extends Command {
    public static final String COMMAND_WORD = "goto";
    public static final String MESSAGE_NAVIGATION_TIME_SUCCESS = "Here is the remark at time %d.%d";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Navigate to a particular time of an interviewee's interview.\n"
            + "Parameters: TIME (in the format mm.ss)\n"
            + "Example:  " + COMMAND_WORD + " 30.00";

    private final Duration timeQuery;

    public NavigationTimeCommand(Duration timeQuery) {
        this.timeQuery = timeQuery;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);

        if (!model.hasCurrentInterviewee()) {
            throw new CommandException("You need to open a transcript of a particular interviewee "
                    + "to go to the answer of a question.");
        }
        int remarkIndex = model.getCurrentTranscript().getIndexAtTime(timeQuery);
        return new NavigationCommandResult(String.format(MESSAGE_NAVIGATION_TIME_SUCCESS,
                timeQuery.toMinutes(), timeQuery.toSecondsPart()), remarkIndex);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NavigationTimeCommand
                && timeQuery.equals(((NavigationTimeCommand) other).timeQuery));
    }
}
