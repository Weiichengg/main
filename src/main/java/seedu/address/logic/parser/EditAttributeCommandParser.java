package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;

import seedu.address.logic.commands.EditAttributeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditAttributeCommand object
 */
public class EditAttributeCommandParser implements Parser<EditAttributeCommand> {

    /*private static final Pattern BASIC_EDIT_ATTRIBUTE_COMMAND_FORMAT =
            Pattern.compile("(?<oldAttribute>\\S+)(?<newAttribute>)");*/

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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttributeCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.arePrefixesPresent(PREFIX_ATTRIBUTE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttributeCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getValue(PREFIX_ATTRIBUTE).get().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttributeCommand.MESSAGE_USAGE));
        }

        return new EditAttributeCommand(
                argMultimap.getPreamble(),
                argMultimap.getValue(PREFIX_ATTRIBUTE).get()
        );
    }


}
