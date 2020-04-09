package hirelah.logic.parser;

import static hirelah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import hirelah.logic.parser.exceptions.ParseException;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /** Checks whether the argument has any content.
     *
     * @param arguments the arguments to be checked
     * @return boolean whether the argument has any content
     */
    public static boolean isEmptyArgument (String arguments) {
        return (arguments.trim().equals(""));
    }

    /** Raises exception when the argument has no content.
     *
     * @param checkedString the message to be checked
     * @param messageUsage the message to be displayed if the string is empty
     *
     * @throws ParseException if there is no content
     */
    public static void checkEmptyArgument (String checkedString, String messageUsage) throws ParseException {
        if (ParserUtil.isEmptyArgument(checkedString)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage)
            );
        }
    }

}
