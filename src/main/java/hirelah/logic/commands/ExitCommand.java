package hirelah.logic.commands;

import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Address Book as requested ...";

    @Override
    public CommandResult execute(Model model, Storage storage) {
        return new ExitCommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExitCommand);
    }
}
