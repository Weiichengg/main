package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;

import hirelah.logic.commands.EditAttributeCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditAttributeCommand object
 */
public class EditAttributeCommandParser implements Parser<EditAttributeCommand> {

    public static final String MESSAGE_INCOMPLETE_ARGUMENT = "Incomplete input format for editing an attribute.\n%s";

    /**
     * Parses the given {@code String} of arguments in the context of the EditAttributeCommand
     * and returns an EditAttributeCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditAttributeCommand parse(String arguments) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_ATTRIBUTE);

        if (argMultimap.getPreamble().equals("")) {
            throw new ParseException(String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditAttributeCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.arePrefixesPresent(PREFIX_ATTRIBUTE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttributeCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getValue(PREFIX_ATTRIBUTE).get().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditAttributeCommand.MESSAGE_USAGE));
        }

        return new EditAttributeCommand(
                argMultimap.getPreamble(),
                argMultimap.getValue(PREFIX_ATTRIBUTE).get()
        );
    }


}
