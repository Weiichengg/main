package hirelah.logic.parser;

import static hirelah.logic.commands.CommandTestUtility.INVALID_DUMMY_VALUE;
import static hirelah.logic.commands.CommandTestUtility.INVALID_QUESTION_BLANK;
import static hirelah.logic.commands.CommandTestUtility.INVALID_QUESTION_NUMBER_1;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_INTEGRITY;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_PERSISTENCE;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_SCORE_1;
import static hirelah.logic.commands.CommandTestUtility.VALID_ATTRIBUTE_SCORE_2;
import static hirelah.logic.commands.CommandTestUtility.VALID_COMMAND_RESUME;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEWEE_JANE;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_ATTRIBUTES;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_END;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_METRICS;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_QUESTIONS;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_SET;
import static hirelah.logic.commands.CommandTestUtility.VALID_INTERVIEW_COMMAND_START;
import static hirelah.logic.commands.CommandTestUtility.VALID_QUESTION_NUMBER_1;
import static hirelah.logic.commands.CommandTestUtility.WHITESPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hirelah.commons.core.Messages;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.ListAttributeCommand;
import hirelah.logic.commands.ListMetricCommand;
import hirelah.logic.commands.ListQuestionCommand;
import hirelah.logic.commands.OpenResumeCommand;
import hirelah.logic.commands.interview.EndCommand;
import hirelah.logic.commands.interview.ScoreCommand;
import hirelah.logic.commands.interview.StartQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;
import hirelah.testutil.Assert;

class InterviewParserTest {

    private InterviewParser parser = new InterviewParser();

    @Test
    void parseCommand_openResume_success() throws ParseException {
        Command result = parser.parseCommand(VALID_COMMAND_RESUME + WHITESPACE + VALID_INTERVIEWEE_JANE);
        assertEquals(result, new OpenResumeCommand("Jane Doe"));
    }

    @Test
    void parseCommand_openResume_failure() throws ParseException {
        Command result = parser.parseCommand(VALID_COMMAND_RESUME + WHITESPACE);
        assertEquals(result, new OpenResumeCommand(""));
    }

    @Test
    void parseCommand_endInterview_success() throws ParseException {
        Command result = parser.parseCommand(VALID_INTERVIEW_COMMAND_END);
        assertEquals(result, new EndCommand());
    }

    @Test
    void parseCommand_listOfAttributes_success() throws ParseException {
        Command result = parser.parseCommand(VALID_INTERVIEW_COMMAND_ATTRIBUTES);
        assertEquals(result, new ListAttributeCommand());
    }

    @Test
    void parseCommand_listOfQuestions_success() throws ParseException {
        Command result = parser.parseCommand(VALID_INTERVIEW_COMMAND_QUESTIONS);
        assertEquals(result, new ListQuestionCommand());
    }

    @Test
    void parseCommand_listOfMetrics_success() throws ParseException {
        Command result = parser.parseCommand(VALID_INTERVIEW_COMMAND_METRICS);
        assertEquals(result, new ListMetricCommand());
    }

    @Test
    void parseCommand_startQuestion_success() throws ParseException {
        Command result = parser.parseCommand(VALID_INTERVIEW_COMMAND_START + WHITESPACE + VALID_QUESTION_NUMBER_1);
        assertEquals(result, new StartQuestionCommand(1));
    }

    @Test
    void parseCommand_startQuestion_failure() throws ParseException {
        Assert.assertThrows(ParseException.class, Messages.INVALID_QUESTION_NUMBER_MESSAGE, () ->
                parser.parseCommand(VALID_INTERVIEW_COMMAND_START + WHITESPACE + INVALID_QUESTION_NUMBER_1));

        Assert.assertThrows(ParseException.class, Messages.INVALID_QUESTION_NUMBER_MESSAGE, () ->
                parser.parseCommand(VALID_INTERVIEW_COMMAND_START
                        + WHITESPACE + INVALID_QUESTION_NUMBER_1 + INVALID_DUMMY_VALUE));

        Assert.assertThrows(ParseException.class, Messages.INVALID_QUESTION_NUMBER_MESSAGE, () ->
                parser.parseCommand(VALID_INTERVIEW_COMMAND_START
                        + WHITESPACE + INVALID_QUESTION_BLANK));
    }

    @Test
    void parseCommand_setAttributeScore_success() throws ParseException {
        Command firstResult = parser.parseCommand(VALID_INTERVIEW_COMMAND_SET + WHITESPACE
                + VALID_ATTRIBUTE_INTEGRITY + WHITESPACE + VALID_ATTRIBUTE_SCORE_1);
        assertEquals(firstResult, new ScoreCommand("integrity", 5.35));

        Command secondResult = parser.parseCommand(VALID_INTERVIEW_COMMAND_SET + WHITESPACE
                + VALID_ATTRIBUTE_PERSISTENCE + WHITESPACE + VALID_ATTRIBUTE_SCORE_2);
        assertEquals(secondResult, new ScoreCommand("persistence", 10));
    }
}
