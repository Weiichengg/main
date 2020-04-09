package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_PERSISTENCE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_ATTRIBUTE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_INTERVIEWEE;
import static hirelah.logic.commands.CommandTestUtility.VALID_PROPERTY_QUESTION;
import static hirelah.logic.commands.CommandTestUtility.VALID_QUESTION_WHAT;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static hirelah.logic.parser.CliSyntax.PREFIX_ALIAS;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static hirelah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import hirelah.logic.commands.AddAttributeCommand;
import hirelah.logic.commands.AddIntervieweeCommand;
import hirelah.logic.commands.AddQuestionCommand;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE
                        + WHITESPACE + VALID_INTERVIEWEE_JANE,
                new AddIntervieweeCommand("Jane Doe"));

        assertParseSuccess(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE
                        + WHITESPACE + VALID_INTERVIEWEE_JANE
                        + WHITESPACE + PREFIX_ALIAS
                        + WHITESPACE + "Jane",
                new AddIntervieweeCommand("Jane Doe", "Jane"));

        assertParseSuccess(parser, WHITESPACE
                        + VALID_PROPERTY_ATTRIBUTE
                        + WHITESPACE
                        + VALID_ATTRIBUTE_PERSISTENCE,
                new AddAttributeCommand("persistence"));

        assertParseSuccess(parser, WHITESPACE
                        + VALID_PROPERTY_QUESTION
                        + WHITESPACE
                        + VALID_QUESTION_WHAT,
                new AddQuestionCommand("What is this question?"));
    }

    @Test
    void parse_compulsoryFieldsMissing_success() {
        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_INTERVIEWEE + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddIntervieweeCommand.MESSAGE_USAGE));
        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_ATTRIBUTE + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAttributeCommand.MESSAGE_USAGE));
        assertParseFailure(parser, WHITESPACE + VALID_PROPERTY_QUESTION + WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddQuestionCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argumentMissing_failure() {
        assertParseFailure(parser, WHITESPACE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommandParser.EXPECTED_INPUT_FORMAT));
    }
}
