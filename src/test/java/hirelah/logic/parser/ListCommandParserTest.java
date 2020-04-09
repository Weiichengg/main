package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ALIAS_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_ATTRIBUTE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_INTERVIEWEE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_QUESTION;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.ListAttributeCommand;
import hirelah.logic.commands.ListIntervieweeCommand;
import hirelah.logic.commands.ListQuestionCommand;

class ListCommandParserTest {

    private ListCommandParser parser = new ListCommandParser();

    @Test
    void parse_validCommand_success() {

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE,
                new ListIntervieweeCommand());

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_ATTRIBUTE,
                new ListAttributeCommand());

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_QUESTION,
                new ListQuestionCommand());
    }

    @Test
    void parse_invalidCommand_success() {

        assertParseFailure(parser, WHITESPACE + VALID_ALIAS_JANE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommandParser.EXPECTED_INPUT_FORMAT));
    }
}
