package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_QUESTION;

import hirelah.logic.commands.EditQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditQuestionCommand object
 */
public class EditQuestionCommandParser implements Parser<EditQuestionCommand> {

    public static final String MESSAGE_INCOMPLETE_ARGUMENT = "Incomplete input format for editing a question.\n%s";
    public static final String INVALID_QUESTION_NUMBER_MESSAGE = "The question number provided is invalid.";

    /**
     * Parses the given {@code String} of arguments in the context of the EditQuestionCommand
     * and returns an EditQuestionCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditQuestionCommand parse(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_QUESTION);

        if (!argMultimap.arePrefixesPresent(PREFIX_QUESTION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditQuestionCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getValue(PREFIX_QUESTION).get().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditQuestionCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getPreamble().equals("")) {
            throw new ParseException(String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditQuestionCommand.MESSAGE_USAGE));
        }

        try {
            int index = Integer.parseInt(argMultimap.getPreamble());
            return new EditQuestionCommand(index, argMultimap.getValue(PREFIX_QUESTION).get());
        } catch (NumberFormatException e) {
            throw new ParseException(INVALID_QUESTION_NUMBER_MESSAGE);
        }
    }
}
