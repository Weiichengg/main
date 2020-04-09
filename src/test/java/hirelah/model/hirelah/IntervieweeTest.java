package hirelah.model.hirelah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;

class IntervieweeTest {

    @Test
    void constructor_invalidName_exceptionThrown() {
        try {
            Interviewee i = new Interviewee("12345", 4); // test numeric name
            fail();
        } catch (IllegalValueException e) {
            assertEquals(Interviewee.MESSAGE_CONSTRAINTS, e.getMessage());
        }
        try {
            Interviewee i = new Interviewee("", 4); // test empty string name
            fail();
        } catch (IllegalValueException e) {
            assertEquals(Interviewee.MESSAGE_CONSTRAINTS, e.getMessage());
        }
    }


    @Test
    void setAlias_validIdentifier_hasAlias() throws IllegalValueException {
        Interviewee i = new Interviewee("Bob", 4);
        i.setAlias("Bo");
        assertTrue(i.getAlias().isPresent());
    }
}
