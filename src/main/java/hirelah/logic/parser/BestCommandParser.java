package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static hirelah.logic.parser.CliSyntax.PREFIX_METRIC;
import static hirelah.model.hirelah.BestParameter.ATTRIBUTE;
import static hirelah.model.hirelah.BestParameter.METRIC;
import static hirelah.model.hirelah.BestParameter.OVERALL;

import java.util.List;

import hirelah.logic.commands.BestCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * BestParser describes the behavior of the parser that is assigned
 * to parse the command for finding best interviewees.
 */

public class BestCommandParser implements Parser<BestCommand> {
    public static final String MESSAGE_MULTIPLE_PARAMETERS_PROVIDED = "Multiple parameters for comparisons provided.";

    /**
     * Parses the argument provided by the client.
     * @param arguments The argument entered by the client.
     * @return The corresponding BestCommand.
     * @throws ParseException If there is an invalid argument entered by the client.
     */

    public BestCommand parse(String arguments) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(arguments, PREFIX_METRIC, PREFIX_ATTRIBUTE);
        if (arguments.trim().equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, BestCommand.MESSAGE_USAGE));
        } else {
            List<String> metricPrefixes = argMultimap.getAllValues(PREFIX_METRIC);
            List<String> attributePrefixes = argMultimap.getAllValues(PREFIX_ATTRIBUTE);

            int numOfParams = getNumberOfTotalParams(metricPrefixes, attributePrefixes);

            if (numOfParams > 1) {
                throw new ParseException(MESSAGE_MULTIPLE_PARAMETERS_PROVIDED);
            } else if (numOfParams == 0) {
                return new BestCommand(argMultimap.getPreamble(), OVERALL);
            } else {
                if (metricPrefixes.size() == 1) {
                    return new BestCommand(argMultimap.getPreamble(), metricPrefixes.get(0), METRIC);
                } else {
                    return new BestCommand(argMultimap.getPreamble(), attributePrefixes.get(0), ATTRIBUTE);
                }
            }
        }
    }

    /**
     * Obtain the number of parameters a client provides.
     * @param metricPrefixes The metrics' prefixes list.
     * @param attributePrefixes The attributes' prefixes lsit.
     * @return The number of total parameters provided by the client.
     */
    private int getNumberOfTotalParams(List<String> metricPrefixes, List<String> attributePrefixes) {
        return metricPrefixes.size() + attributePrefixes.size();
    }
}
