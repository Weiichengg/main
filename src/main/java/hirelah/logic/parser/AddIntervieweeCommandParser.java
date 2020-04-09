package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_ALIAS;

import hirelah.logic.commands.AddIntervieweeCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new object of type AddIntervieweeCommand
 */
public class AddIntervieweeCommandParser implements Parser<AddIntervieweeCommand> {

    private static final String MESSAGE_INCOMPLETE_ARGUMENT = "Missing argument for alias of the interviewee.\n%s";

    /**
     * Parses the given {@code String} of arguments in the context of the AddIntervieweeCommand
     * and returns an AddIntervieweeCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddIntervieweeCommand parse(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_ALIAS);

        if (arguments.equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddIntervieweeCommand.MESSAGE_USAGE));
        } else if (!argMultimap.arePrefixesPresent(PREFIX_ALIAS)) {
            return new AddIntervieweeCommand(arguments);
        } else {
            if (argMultimap.getValue(PREFIX_ALIAS).get().equals("")) {
                throw new ParseException(
                        String.format(MESSAGE_INCOMPLETE_ARGUMENT, AddIntervieweeCommand.MESSAGE_USAGE));
            }
            return new AddIntervieweeCommand(argMultimap.getPreamble(),
                    argMultimap.getValue(PREFIX_ALIAS).get());
        }
    }
}
