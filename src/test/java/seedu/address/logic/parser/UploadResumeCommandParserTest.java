package seedu.address.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PATH_DOWNLOADS;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_PATH;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.UploadResumeCommand;
import hirelah.logic.parser.UploadResumeCommandParser;

class UploadResumeCommandParserTest {

    private UploadResumeCommandParser parser = new UploadResumeCommandParser();

    @Test
    void parse_noPathField_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE,
                new UploadResumeCommand("Jane Doe", null));
    }

    @Test
    void parse_withPathField_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE + WHITESPACE
                        + PREFIX_PATH + WHITESPACE + VALID_PATH_DOWNLOADS,
                new UploadResumeCommand("Jane Doe", "/Downloads"));
    }

    @Test
    public void parse_argumentMissing_failure() {
        assertParseFailure(parser, WHITESPACE + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UploadResumeCommand.MESSAGE_USAGE));
    }
}
