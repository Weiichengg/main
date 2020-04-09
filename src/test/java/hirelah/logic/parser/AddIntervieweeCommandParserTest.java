package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ALIAS_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_ALIAS;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.AddIntervieweeCommand;

class AddIntervieweeCommandParserTest {
    private AddIntervieweeCommandParser parser = new AddIntervieweeCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        assertParseSuccess(parser, VALID_INTERVIEWEE_JANE,
                new AddIntervieweeCommand("Jane Doe"));

        assertParseSuccess(parser, WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_ALIAS
                        + WHITESPACE + VALID_ALIAS_JANE,
                new AddIntervieweeCommand("Jane Doe", "Jane"));

    }

    @Test
    public void parse_argumentMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddIntervieweeCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

}
