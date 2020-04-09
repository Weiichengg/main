package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hirelah.logic.commands.Command;
import hirelah.logic.commands.DeleteAttributeCommand;
import hirelah.logic.commands.DeleteIntervieweeCommand;
import hirelah.logic.commands.DeleteMetricCommand;
import hirelah.logic.commands.DeleteQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<Command> {

    public static final String EXPECTED_INPUT_FORMAT =
            DeleteAttributeCommand.MESSAGE_FORMAT + DeleteAttributeCommand.MESSAGE_FUNCTION
            + DeleteIntervieweeCommand.MESSAGE_FORMAT + DeleteIntervieweeCommand.MESSAGE_FUNCTION
            + DeleteMetricCommand.MESSAGE_FORMAT + DeleteMetricCommand.MESSAGE_FUNCTION
            + DeleteQuestionCommand.MESSAGE_FORMAT + DeleteQuestionCommand.MESSAGE_FUNCTION;

    public static final String INVALID_QUESTION_NUMBER_MESSAGE = "The question number provided is invalid.";

    private static final Pattern BASIC_DELETE_COMMAND_FORMAT =
            Pattern.compile("(?<deleteCommandWord>\\S+)(?<deleteArguments>.*)");

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String arguments) throws ParseException {
        Matcher matcher = BASIC_DELETE_COMMAND_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EXPECTED_INPUT_FORMAT));
        }
        final String deleteCommandWord = matcher.group("deleteCommandWord");
        final String deleteArguments = matcher.group("deleteArguments");

        switch (deleteCommandWord) {
        case DeleteAttributeCommand.COMMAND_WORD:
            ParserUtil.checkEmptyArgument(deleteArguments, DeleteAttributeCommand.MESSAGE_USAGE);
            return new DeleteAttributeCommand(deleteArguments.trim());

        case DeleteIntervieweeCommand.COMMAND_WORD:
            ParserUtil.checkEmptyArgument(deleteArguments, DeleteIntervieweeCommand.MESSAGE_USAGE);
            return new DeleteIntervieweeCommand(deleteArguments.trim());

        case DeleteQuestionCommand.COMMAND_WORD:
            ParserUtil.checkEmptyArgument(deleteArguments, DeleteQuestionCommand.MESSAGE_USAGE);

            try {
                int index = Integer.parseInt(deleteArguments.trim());
                return new DeleteQuestionCommand(index);
            } catch (NumberFormatException e) {
                throw new ParseException(INVALID_QUESTION_NUMBER_MESSAGE);
            }

        case DeleteMetricCommand.COMMAND_WORD:
            return new DeleteMetricCommand(deleteArguments.trim());

        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EXPECTED_INPUT_FORMAT));
        }
    }
}
