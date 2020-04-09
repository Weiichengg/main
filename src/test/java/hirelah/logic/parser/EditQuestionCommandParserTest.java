package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.INVALID_QUESTION_NUMBER_1;
import static hirelah.logic.commands.CommandTestUtility.VALID_NUMBER_1;
import static hirelah.logic.commands.CommandTestUtility.VALID_QUESTION_WHAT;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_QUESTION;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static hirelah.logic.parser.EditQuestionCommandParser.INVALID_QUESTION_NUMBER_MESSAGE;
import static hirelah.logic.parser.EditQuestionCommandParser.MESSAGE_INCOMPLETE_ARGUMENT;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.EditQuestionCommand;

class EditQuestionCommandParserTest {

    private EditQuestionCommandParser parser = new EditQuestionCommandParser();

    @Test
    void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_NUMBER_1
                        + WHITESPACE + PREFIX_QUESTION
                        + WHITESPACE + VALID_QUESTION_WHAT,
                new EditQuestionCommand(1, VALID_QUESTION_WHAT));
    }

    @Test
    void parse_questionNumberFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + PREFIX_QUESTION
                        + WHITESPACE + VALID_QUESTION_WHAT,
                String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditQuestionCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_invalidQuestionNumber_failure() {
        assertParseFailure(parser, WHITESPACE + INVALID_QUESTION_NUMBER_1
                        + WHITESPACE + PREFIX_QUESTION
                        + WHITESPACE + VALID_QUESTION_WHAT,
                INVALID_QUESTION_NUMBER_MESSAGE);
    }

    @Test
    void parse_questionDescriptionFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_NUMBER_1
                        + WHITESPACE + PREFIX_QUESTION
                        + WHITESPACE,
                String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditQuestionCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_prefixMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_NUMBER_1
                        + WHITESPACE + VALID_QUESTION_WHAT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditQuestionCommand.MESSAGE_USAGE));
    }
}
