package hirelah.logic.commands.presession;

import java.io.File;
import java.nio.file.Path;

import hirelah.commons.exceptions.DataConversionException;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.AppPhase;
import hirelah.storage.Storage;

/**
 * Attempts to open the given session.
 */
public class OpenSessionCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Opened session: %s";
    public static final String MESSAGE_NO_SUCH_SESSION = "No such session exists!";
    public static final String MESSAGE_FAILED = "Failed to open the session! Some data may have been corrupted";

    private String sessionName;

    public OpenSessionCommand(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        File sessionDir = new File(model.getSessionsDirectory().toFile(), sessionName);
        if (!sessionDir.isDirectory()) {
            throw new CommandException(MESSAGE_NO_SUCH_SESSION);
        }

        Path session = sessionDir.toPath();
        try {
            storage.loadSession(model, session);
            model.setCurrentSession(session);
            model.setAppPhase(AppPhase.NORMAL);
        } catch (DataConversionException e) {
            throw new CommandException(MESSAGE_FAILED);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, sessionName));
    }
}
