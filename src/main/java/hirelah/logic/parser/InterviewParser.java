package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hirelah.logic.commands.Command;
import hirelah.logic.commands.HelpCommand;
import hirelah.logic.commands.OpenResumeCommand;
import hirelah.logic.commands.interview.EndCommand;
import hirelah.logic.commands.interview.RemarkCommand;
import hirelah.logic.commands.interview.ScoreCommand;
import hirelah.logic.commands.interview.StartQuestionCommand;
import hirelah.logic.parser.exceptions.ParseException;

/*
 * InterviewParser
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 13 Mar 2020
 *
 */

/**
 * InterviewParser parses the input entered by the client
 * when HireLah! is not in interviewing mode.
 */
public class InterviewParser {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Pattern SCORE_COMMAND_FORMAT =
            Pattern.compile(":(?<attribute>[\\p{Alpha}][\\p{Alpha} ]*?)\\s+(?<score>\\d+(\\.\\d+)?)");
    private static final Pattern START_QUESTION_COMMAND_FORMAT =
            Pattern.compile(":start q(?<questionNumber>\\d+)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        if (userInput.startsWith(":")) {
            return parseSpecialCommand(userInput);
        } else if (userInput.startsWith("resume")) {
            final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
            if (!matcher.matches()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
            }

            final String arguments = matcher.group("arguments");
            return new OpenResumeCommand(arguments.trim());
        }
        return new RemarkCommand(userInput);
    }

    /**
     * Parses non-remark commands during the interview.
     *
     * @param userInput the user input.
     * @return the parsed command.
     * @throws ParseException if the command word cannot be identified with any command or attribute to score.
     */
    private Command parseSpecialCommand(String userInput) throws ParseException {
        if (userInput.equals(":end")) {
            return new EndCommand();
        }
        Matcher startQuestionMatcher = START_QUESTION_COMMAND_FORMAT.matcher(userInput.trim());
        if (startQuestionMatcher.matches()) {
            int questionNumber = Integer.parseInt(startQuestionMatcher.group("questionNumber"));
            return new StartQuestionCommand(questionNumber);
        }
        Matcher scoreMatcher = SCORE_COMMAND_FORMAT.matcher(userInput.trim());
        if (scoreMatcher.matches()) {
            double score = Double.parseDouble(scoreMatcher.group("score"));
            return new ScoreCommand(scoreMatcher.group("attribute"), score);
        }

        throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
    }
}
