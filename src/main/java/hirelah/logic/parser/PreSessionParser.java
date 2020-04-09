package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hirelah.logic.commands.Command;
import hirelah.logic.commands.HelpCommand;
import hirelah.logic.commands.presession.DeleteSessionCommand;
import hirelah.logic.commands.presession.NewSessionCommand;
import hirelah.logic.commands.presession.OpenSessionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * PreSessionParser parses user input before a session is set.
 */
public class PreSessionParser {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final HashMap<String, CommandSupplier> suppliers = new HashMap<>() {
        {
            put("open", args -> new OpenSessionCommand(args.trim()));
            put("new", args -> new NewSessionCommand(args.trim()));
            put("delete", args -> new DeleteSessionCommand(args.trim()));
            put("help", args -> new HelpCommand());
        }
    };

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        if (suppliers.containsKey(commandWord)) {
            return suppliers.get(commandWord).getCommand(arguments);
        } else {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
