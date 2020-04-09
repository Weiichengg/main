package hirelah.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import hirelah.logic.parser.exceptions.ParseException;

class CloseSessionCommandParserTest {
    private CloseSessionCommandParser parser = new CloseSessionCommandParser();

    @Test
    void parse_session_success() {
        assertDoesNotThrow(() -> parser.parse("session"));
    }

    @Test
    void parse_blank_throwsException() {
        try {
            parser.parse("");
            fail();
        } catch (ParseException e) {
            assertEquals(CloseSessionCommandParser.MESSAGE_FAILURE, e.getMessage());
        }
    }
}
