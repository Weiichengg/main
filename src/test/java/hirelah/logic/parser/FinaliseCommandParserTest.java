package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.INVALID_DUMMY_VALUE;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.FinaliseCommand;

class FinaliseCommandParserTest {

    private FinaliseCommandParser parser = new FinaliseCommandParser();

    @Test
    void parse_noFieldsPresent_success() {
        assertParseSuccess(parser, "",
                new FinaliseCommand());
    }

    @Test
    void parse_fieldsPresent_failure() {
        assertParseFailure(parser, WHITESPACE + INVALID_DUMMY_VALUE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FinaliseCommand.MESSAGE_USAGE));
    }
}
