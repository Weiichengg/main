package hirelah.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * NavigationTimeCommand describes the behavior when the
 * client wants to navigate to a certain remark of the interview.
 */
public class NavigationTimeCommand extends Command {

    public static final String COMMAND_WORD = ".";
    public static final String MESSAGE_FORMAT = "goto <minutes>" + COMMAND_WORD + "<seconds>";
    public static final String MESSAGE_FUNCTION = ": Navigate to a particular time of an interviewee's interview.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Parameters: TIME (in the format mm.ss)\n"
            + "Example:  " + COMMAND_WORD + " 30.00";

    public static final String MESSAGE_NAVIGATION_TIME_SUCCESS = "Here is the remark at time %d.%s";

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
                timeQuery.toMinutes(), String.format("%02d", timeQuery.toSecondsPart())), remarkIndex);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NavigationTimeCommand
                && timeQuery.equals(((NavigationTimeCommand) other).timeQuery));
    }
}
