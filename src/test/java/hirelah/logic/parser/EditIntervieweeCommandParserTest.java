package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ALIAS_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANICE;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_ALIAS;
import static hirelah.logic.parser.CliSyntax.PREFIX_NAME;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static hirelah.logic.parser.EditIntervieweeCommandParser.MESSAGE_INCOMPLETE_ARGUMENT;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.EditIntervieweeCommand;

class EditIntervieweeCommandParserTest {

    private EditIntervieweeCommandParser parser = new EditIntervieweeCommandParser();

    @Test
    void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_NAME + VALID_INTERVIEWEE_JANICE,
                new EditIntervieweeCommand("Jane Doe", "Janice Doe", ""));

        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_ALIAS + VALID_ALIAS_JANE,
                new EditIntervieweeCommand("Jane Doe", "", "Jane"));

    }

    @Test
    void parse_oldFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + PREFIX_ALIAS + VALID_ALIAS_JANE,
                String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditIntervieweeCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_newFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_NAME,
                String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditIntervieweeCommand.MESSAGE_USAGE));

        assertParseFailure(parser, WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_ALIAS,
                String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditIntervieweeCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_prefixMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_INTERVIEWEE_JANE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditIntervieweeCommand.MESSAGE_USAGE));
    }

}
