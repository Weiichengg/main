package hirelah.logic.parser;

import hirelah.logic.commands.CloseSessionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Checks that the user typed "close session", then returns a {@code CloseSessionCommand} if successful.
 */
public class CloseSessionCommandParser implements Parser<CloseSessionCommand> {
    public static final String MESSAGE_FAILURE =
            "Invalid command! To close the session, give the command: close session";

    @Override
    public CloseSessionCommand parse(String userInput) throws ParseException {
        if (!userInput.equals("session")) {
            throw new ParseException(MESSAGE_FAILURE);
        }
        return new CloseSessionCommand();
    }
}
