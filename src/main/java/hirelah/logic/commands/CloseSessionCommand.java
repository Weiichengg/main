package hirelah.logic.commands;

import java.nio.file.Path;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * Closes the current session.
 */
public class CloseSessionCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Closed the current session: %s";
    public static final String COMMAND_WORD = "close session";

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        Path session = model.closeSession();
        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, session), ToggleView.SESSION);
    }
}
