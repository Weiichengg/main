package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import hirelah.logic.commands.OpenReportCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input command and creates a new OpenReportCommand object
 */
public class OpenReportCommandParser implements Parser<OpenReportCommand> {

    /**
     * Parses the given {@code String} of identifier in the context of the OpenReportCommand
     * and returns an OpenReportCommand object for execution.
     *
     * @param arguments the identifier of the Interviewee
     * @throws ParseException if the user input is not a valid command word
     */
    public OpenReportCommand parse(String arguments) throws ParseException {

        if (arguments.trim().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, OpenReportCommand.MESSAGE_USAGE));
        }
        return new OpenReportCommand(arguments.trim());
    }
}
