package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_PERSISTENCE;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_TEAM_WORK;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.EditAttributeCommand;

class EditAttributeCommandParserTest {

    private EditAttributeCommandParser parser = new EditAttributeCommandParser();

    @Test
    void parse_allFieldsPresent_success() {

        assertParseSuccess(parser, WHITESPACE + VALID_ATTRIBUTE_TEAM_WORK
                        + WHITESPACE + PREFIX_ATTRIBUTE
                        + WHITESPACE + VALID_ATTRIBUTE_PERSISTENCE,
                new EditAttributeCommand("team work", "persistence"));
    }

    @Test
    void parse_oldFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + PREFIX_ATTRIBUTE
                        + WHITESPACE + VALID_ATTRIBUTE_PERSISTENCE,
                String.format(EditAttributeCommandParser.MESSAGE_INCOMPLETE_ARGUMENT,
                        EditAttributeCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_newFieldMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_ATTRIBUTE_PERSISTENCE
                        + WHITESPACE + PREFIX_ATTRIBUTE
                        + WHITESPACE,
                String.format(EditAttributeCommandParser.MESSAGE_INCOMPLETE_ARGUMENT,
                        EditAttributeCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_prefixMissing_failure() {
        assertParseFailure(parser, WHITESPACE + VALID_ATTRIBUTE_PERSISTENCE
                        + WHITESPACE + VALID_ATTRIBUTE_TEAM_WORK,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttributeCommand.MESSAGE_USAGE));
    }
}
