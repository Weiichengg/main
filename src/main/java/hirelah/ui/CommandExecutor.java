package hirelah.ui;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.Logic;
import hirelah.logic.commands.exceptions.CommandException;

/**
 * Represents a function that can execute commands.
 */
@FunctionalInterface
public interface CommandExecutor {
    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    void execute(String commandText) throws CommandException, IllegalValueException;
}
