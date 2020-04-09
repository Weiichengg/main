package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_INTEGRITY;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_PERSISTENCE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANICE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_ATTRIBUTE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_INTERVIEWEE;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static hirelah.logic.parser.CliSyntax.PREFIX_NAME;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.EditAttributeCommand;
import hirelah.logic.commands.EditIntervieweeCommand;

class EditCommandParserTest {
    private EditCommandParser parser = new EditCommandParser();

    @Test
    void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE
                        + WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_NAME + VALID_INTERVIEWEE_JANICE,
                new EditIntervieweeCommand("Jane Doe", "Janice Doe", ""));

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_ATTRIBUTE
                        + WHITESPACE + VALID_ATTRIBUTE_PERSISTENCE
                        + WHITESPACE + PREFIX_ATTRIBUTE
                        + WHITESPACE + VALID_ATTRIBUTE_INTEGRITY,
                new EditAttributeCommand("persistence", "integrity"));
    }

    //    @Test
    //    void parse_compulsoryFieldsMissing_success() {
    //
    //        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE + WHITESPACE,
    //                MESSAGE_UNKNOWN_COMMAND);
    //
    //        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_ATTRIBUTE + WHITESPACE,
    //                MESSAGE_UNKNOWN_COMMAND);
    //
    //        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_QUESTION + WHITESPACE,
    //                MESSAGE_UNKNOWN_COMMAND);
    //    }

    @Test
    public void parse_argumentMissing_failure() {
        assertParseFailure(parser, WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommandParser.EXPECTED_INPUT_FORMAT));
    }
}
