package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hirelah.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static hirelah.logic.parser.CliSyntax.PREFIX_NAME;
import static hirelah.logic.parser.CliSyntax.PREFIX_WEIGHTAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hirelah.logic.commands.EditMetricCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new object of type EditMetricCommand
 */

public class EditMetricCommandParser implements Parser<EditMetricCommand> {
    private static final String MESSAGE_INCOMPLETE_ARGUMENT = "Missing name, attribute and weightage details.\n%s";
    private static final String MESSAGE_INVALID_WEIGHTAGE_FORMAT = "There is an invalid format of the weightage."
            + "Please ensure your weightage are in numbers.";
    /**
     * Parses the given {@code String} of arguments in the context of the EditMetricCommand
     * and returns an EditMetricCommand object for execution.
     *
     * @param arguments the arguments to be parsed
     * @throws ParseException if the user input does not conform the expected format
     */

    public EditMetricCommand parse(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_NAME, PREFIX_ATTRIBUTE, PREFIX_WEIGHTAGE);

        if (arguments.equals("")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditMetricCommand.MESSAGE_USAGE));
        } else if (!argMultimap.arePrefixesPresent(PREFIX_WEIGHTAGE, PREFIX_ATTRIBUTE)
                && argMultimap.arePrefixesPresent(PREFIX_NAME)) {
            return new EditMetricCommand(argMultimap.getPreamble(), argMultimap.getValue(PREFIX_NAME).get(),
                    new ArrayList<>(), new ArrayList<>());
        } else if (argMultimap.arePrefixesPresent(PREFIX_WEIGHTAGE, PREFIX_ATTRIBUTE)) {
            List<String> weightages = argMultimap.getAllValues(PREFIX_WEIGHTAGE);
            try {
                List<Double> castedWeightages = weightages.stream().map(Double::valueOf).collect(Collectors.toList());
                return new EditMetricCommand(argMultimap.getPreamble(),
                        argMultimap.arePrefixesPresent(PREFIX_NAME) ? argMultimap.getValue(PREFIX_NAME).get()
                                                                    : "",
                        argMultimap.getAllValues(PREFIX_ATTRIBUTE), castedWeightages);
            } catch (NumberFormatException e) {
                throw new ParseException(MESSAGE_INVALID_WEIGHTAGE_FORMAT);
            }
        } else {
            throw new ParseException(String.format(MESSAGE_INCOMPLETE_ARGUMENT, EditMetricCommand.MESSAGE_USAGE));
        }
    }
}
