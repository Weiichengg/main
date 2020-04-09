package hirelah.logic.parser;

import static hirelah.logic.parser.CliSyntax.PREFIX_PATH;

import hirelah.logic.commands.UploadResumeCommand;
import hirelah.logic.parser.exceptions.ParseException;

/**
 * Parses the UploadResumeCommand with the identifier and optionally the path to the file.
 */
public class UploadResumeCommandParser implements Parser<UploadResumeCommand> {

    @Override
    public UploadResumeCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_PATH);
        String path = argMultimap.getValue(PREFIX_PATH).orElse(null);
        return new UploadResumeCommand(argMultimap.getPreamble(), path);
    }
}
