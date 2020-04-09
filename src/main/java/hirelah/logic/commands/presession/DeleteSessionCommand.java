package hirelah.logic.commands.presession;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import hirelah.commons.util.StringUtil;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.ToggleCommandResult;
import hirelah.logic.commands.ToggleView;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * Deletes the specified session, with all of its data.
 */
public class DeleteSessionCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_SUCCESS = "Successfully deleted the session: %s";
    public static final String MESSAGE_NO_SUCH_SESSION = "No such session exists!";
    public static final String MESSAGE_FORMAT = "delete <session>";
    public static final String MESSAGE_USAGE = MESSAGE_FORMAT + ": deletes the given session";

    private String sessionName;

    public DeleteSessionCommand(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        File sessionDir = new File(model.getSessionsDirectory().toFile(), sessionName);
        if (!sessionDir.isDirectory()) {
            throw new CommandException(MESSAGE_NO_SUCH_SESSION);
        }
        try {
            deleteSession(sessionDir.toPath());
        } catch (IOException e) {
            throw new CommandException("A problem occurred while deleting: " + StringUtil.getDetails(e));
        }
        return new ToggleCommandResult(String.format(MESSAGE_SUCCESS, sessionName), ToggleView.SESSION);
    }

    /**
     * Recursively deletes an entire session directory.
     *
     * @param session The session to delete.
     * @throws IOException If an error occurs while deleting.
     */
    private void deleteSession(Path session) throws IOException {
        // Solution below entirely adopted from
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/FileVisitor.html
        Files.walkFileTree(session, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        });
    }
}
