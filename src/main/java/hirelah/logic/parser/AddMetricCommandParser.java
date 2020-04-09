package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static hirelah.logic.parser.CliSyntax.PREFIX_WEIGHTAGE;

import java.util.List;
import java.util.stream.Collectors;

import hirelah.logic.commands.AddMetricCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new object of type AddMetricCommand
 */
public class AddMetricCommandParser implements Parser<AddMetricCommand> {
    private static final String MESSAGE_INCOMPLETE_ARGUMENT = "Missing attribute and weightage details.\n%s";
    private static final String MESSAGE_INVALID_WEIGHTAGE_FORMAT = "There is an invalid format of the weightage."
            + "Please ensure your weightage are in numbers.";

    /**
     * Parses the given {@code String} of arguments in the context of the AddMetricCommand
     * and returns an AddMetricCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMetricCommand parse(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_ATTRIBUTE, PREFIX_WEIGHTAGE);

        if (arguments.equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMetricCommand.MESSAGE_USAGE));
        } else if (!argMultimap.arePrefixesPresent(PREFIX_ATTRIBUTE)
                || !argMultimap.arePrefixesPresent(PREFIX_WEIGHTAGE)) {
            throw new ParseException(
                    String.format(MESSAGE_INCOMPLETE_ARGUMENT, AddMetricCommand.MESSAGE_USAGE));
        } else {
            List<String> weightages = argMultimap.getAllValues(PREFIX_WEIGHTAGE);
            try {
                List<Double> castedWeightages = weightages.stream()
                        .map(Double::valueOf)
                        .collect(Collectors.toList());
                return new AddMetricCommand(argMultimap.getPreamble(),
                        argMultimap.getAllValues(PREFIX_ATTRIBUTE),
                        castedWeightages);
            } catch (NumberFormatException e) {
                throw new ParseException(MESSAGE_INVALID_WEIGHTAGE_FORMAT);
            }
        }
    }
}
