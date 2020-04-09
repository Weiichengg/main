package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import hirelah.logic.commands.Command;
import hirelah.logic.commands.ListAttributeCommand;
import hirelah.logic.commands.ListIntervieweeCommand;
import hirelah.logic.commands.ListMetricCommand;
import hirelah.logic.commands.ListQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input command and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<Command> {

    public static final String EXPECTED_INPUT_FORMAT =
            ListAttributeCommand.MESSAGE_FORMAT + ListAttributeCommand.MESSAGE_FUNCTION
            + ListIntervieweeCommand.MESSAGE_FORMAT + ListIntervieweeCommand.MESSAGE_FUNCTION
            + ListMetricCommand.MESSAGE_FORMAT + ListMetricCommand.MESSAGE_FUNCTION
            + ListQuestionCommand.MESSAGE_FORMAT + ListQuestionCommand.MESSAGE_FUNCTION;

    /**
     * Parses the given {@code String} of commandWord in the context of the ListCommand
     * and returns an ListCommand object for execution.
     *
     * @param commandWord the command word to be parsed
     * @throws ParseException if the user input is not a valid command word
     */
    public Command parse(String commandWord) throws ParseException {

        final String listCommandWord = commandWord.trim().toLowerCase();

        switch (listCommandWord) {
        case ListAttributeCommand.COMMAND_WORD:
            return new ListAttributeCommand();

        case ListIntervieweeCommand.COMMAND_WORD:
            return new ListIntervieweeCommand();

        case ListMetricCommand.COMMAND_WORD:
            return new ListMetricCommand();

        case ListQuestionCommand.COMMAND_WORD:
            return new ListQuestionCommand();

        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EXPECTED_INPUT_FORMAT));
        }
    }

}
