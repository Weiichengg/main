package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import hirelah.logic.commands.FinaliseCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FinaliseCommand object
 */
public class FinaliseCommandParser implements Parser<FinaliseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FinaliseCommand
     * and returns a FinaliseCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public FinaliseCommand parse(String arguments) throws ParseException {
        if (!arguments.trim().equals("")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FinaliseCommand.MESSAGE_USAGE));
        }
        return new FinaliseCommand();
    }

}
