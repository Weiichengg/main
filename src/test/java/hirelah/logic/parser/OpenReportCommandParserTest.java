package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.OpenReportCommand;

class OpenReportCommandParserTest {

    private OpenReportCommandParser parser = new OpenReportCommandParser();

    @Test
    void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE,
                new OpenReportCommand("Jane Doe"));
    }

    @Test
    void parse_fieldsMissing_failure() {
        assertParseFailure(parser, WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OpenReportCommand.MESSAGE_USAGE));
    }
}
