package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.INVALID_QUESTION_NUMBER_1;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_PERSISTENCE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_ATTRIBUTE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_INTERVIEWEE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_QUESTION;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.DeleteAttributeCommand;
import hirelah.logic.commands.DeleteIntervieweeCommand;
import hirelah.logic.commands.DeleteQuestionCommand;

class DeleteCommandParserTest {
    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    void parse_allFieldsPresent_success() {

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE
                        + WHITESPACE + VALID_INTERVIEWEE_JANE,
                new DeleteIntervieweeCommand("Jane Doe"));

        assertParseSuccess(parser, WHITESPACE
                        + VALID_PROPERTY_ATTRIBUTE
                        + WHITESPACE
                        + VALID_ATTRIBUTE_PERSISTENCE,
                new DeleteAttributeCommand("persistence"));
    }

    @Test
    void parse_compulsoryFieldsMissing_success() {
        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteIntervieweeCommand.MESSAGE_USAGE));

        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_ATTRIBUTE + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAttributeCommand.MESSAGE_USAGE));

        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_QUESTION + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteQuestionCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_invalidField_success() {

        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_QUESTION + WHITESPACE + INVALID_QUESTION_NUMBER_1,
                DeleteCommandParser.INVALID_QUESTION_NUMBER_MESSAGE);
    }

    @Test
    public void parse_argumentMissing_failure() {
        assertParseFailure(parser, WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommandParser.EXPECTED_INPUT_FORMAT));
    }
}
