package hirelah.logic.commands;

import static hirelah.logic.parser.CliSyntax.PREFIX_ALIAS;
import static java.util.Objects.requireNonNull;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import hirelah.storage.Storage;

/**
 * AddIntervieweeCommand describes the behavior when the
 * client wants to add an interviewee to the list.
 */

public class AddIntervieweeCommand extends Command {

    public static final String COMMAND_WORD = "interviewee";
    public static final String MESSAGE_SUCCESS = "New interviewee added: %1$s";
    public static final String MESSAGE_FORMAT = "add " + COMMAND_WORD + " <full name> [-aka <alias>]";
    public static final String MESSAGE_FUNCTION = ": Adds an interviewee to the Interviewee list.\n";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT
            + MESSAGE_FUNCTION
            + "Example: add " + COMMAND_WORD
            + " Jane Doe "
            + PREFIX_ALIAS + " Doe";

    public static final String EMPTY_STRING = "";
    private final String fullName;
    private final String alias;

    /**
     * Creates an AddIntervieweeCommand to add the specified {@code Interviewee}
     */
    public AddIntervieweeCommand(String fullName, String... optionalAlias) {
        this.fullName = fullName;
        if (optionalAlias.length != 0) {
            this.alias = optionalAlias[0];
        } else {
            this.alias = "";
        }
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        IntervieweeList interviewees = model.getIntervieweeList();

        try {
            if (isEmptyAlias()) {
                interviewees.addInterviewee(fullName);
            } else {
                interviewees.addIntervieweeWithAlias(fullName, alias);
            }
        } catch (IllegalActionException | IllegalValueException e) {
            throw new CommandException(e.getMessage());
        }

        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, fullName), ToggleView.INTERVIEWEE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddIntervieweeCommand // instanceof handles nulls
                && fullName.equals(((AddIntervieweeCommand) other).fullName)
                && alias.equals(((AddIntervieweeCommand) other).alias));
    }

    private boolean isEmptyAlias() {
        return this.alias.equals(EMPTY_STRING);
    }
}
