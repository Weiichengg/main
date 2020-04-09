package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hirelah.logic.commands.Command;
import hirelah.logic.commands.EditAttributeCommand;
import hirelah.logic.commands.EditIntervieweeCommand;
import hirelah.logic.commands.EditMetricCommand;
import hirelah.logic.commands.EditQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<Command> {

    public static final String EXPECTED_INPUT_FORMAT =
            EditAttributeCommand.MESSAGE_FORMAT + EditAttributeCommand.MESSAGE_FUNCTION
            + EditIntervieweeCommand.MESSAGE_FORMAT + EditIntervieweeCommand.MESSAGE_FUNCTION
            + EditMetricCommand.MESSAGE_FORMAT + EditMetricCommand.MESSAGE_FUNCTION
            + EditQuestionCommand.MESSAGE_FORMAT + EditQuestionCommand.MESSAGE_FUNCTION;

    private static final Pattern BASIC_EDIT_COMMAND_FORMAT =
            Pattern.compile("(?<editCommandWord>\\S+) (?<editArguments>.+)");

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String arguments) throws ParseException {
        Matcher matcher = BASIC_EDIT_COMMAND_FORMAT.matcher(arguments.trim());

        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EXPECTED_INPUT_FORMAT));
        }

        final String editCommandWord = matcher.group("editCommandWord");
        final String editArguments = matcher.group("editArguments");

        switch (editCommandWord) {
        case EditAttributeCommand.COMMAND_WORD:
            return new EditAttributeCommandParser().parse(editArguments);

        case EditIntervieweeCommand.COMMAND_WORD:
            return new EditIntervieweeCommandParser().parse(editArguments);

        case EditQuestionCommand.COMMAND_WORD:
            return new EditQuestionCommandParser().parse(editArguments);

        case EditMetricCommand.COMMAND_WORD:
            return new EditMetricCommandParser().parse(editArguments);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EXPECTED_INPUT_FORMAT));
        }
    }

}
