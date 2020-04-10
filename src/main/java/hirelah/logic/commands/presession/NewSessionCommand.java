package hirelah.logic.commands.presession;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import hirelah.commons.exceptions.DataConversionException;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.ToggleCommandResult;
import hirelah.logic.commands.ToggleView;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.model.hirelah.AppPhase;
import hirelah.storage.Storage;

/**
 * Attempts to create a new session with the given name, then loads into that session.
 */
public class NewSessionCommand extends Command {
    public static final String MESSAGE_SUCCESS = "Opened session: %s";
    public static final String MESSAGE_DUPLICATE = "A session with that name already exists!";
    public static final String MESSAGE_ILLEGAL_PATH =
            "Cannot create a directory with this name! Please stick to Alphanumeric characters or spaces to be safe";

    private String sessionName;

    public NewSessionCommand(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        try {
            Path session = model.getSessionsDirectory().resolve(sessionName);
            if (session.toFile().exists()) {
                throw new CommandException(MESSAGE_DUPLICATE);
            }
            storage.loadSession(model, session);
            model.setCurrentSession(session);
            model.setAppPhase(AppPhase.NORMAL);
        } catch (InvalidPathException e) {
            throw new CommandException(MESSAGE_ILLEGAL_PATH);
        } catch (DataConversionException e) {
            throw new CommandException(e.getMessage());
        }

        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, sessionName), ToggleView.INTERVIEW);
    }
}
