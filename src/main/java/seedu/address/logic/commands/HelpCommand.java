package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * Opens the User Guide to help the user.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(Model model, Storage storage) {
        return new HelpCommandResult(SHOWING_HELP_MESSAGE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof HelpCommand);
    }

}
