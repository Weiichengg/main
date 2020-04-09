package hirelah.model.hirelah;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.exceptions.IllegalActionException;

class IntervieweeListTest {

    @Test
    void addInterviewee_validNames_intervieweeAdded() throws IllegalValueException, IllegalActionException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addInterviewee("Sarah O'Conner");
        interviewees.addInterviewee("King Henry the 3rd");
        interviewees.addInterviewee("-155"); // Allow anything as long as it is not parsed by Integer#parseUnsignedInt
        assertEquals("Sarah O'Conner", interviewees.getInterviewee("1").getFullName());
        assertEquals("King Henry the 3rd", interviewees.getInterviewee("2").getFullName());
    }

    @Test
    void addInterviewee_invalidNames_exceptionThrown() {
        IntervieweeList interviewees = new IntervieweeList();
        try {
            interviewees.addInterviewee("15"); // numeric names are disallowed
        } catch (IllegalValueException e) {
            assertEquals(Interviewee.MESSAGE_CONSTRAINTS, e.getMessage());
        }
        try {
            interviewees.addInterviewee(""); // empty strings are disallowed
        } catch (IllegalValueException e) {
            assertEquals(Interviewee.MESSAGE_CONSTRAINTS, e.getMessage());
        }
        assertTrue(interviewees.getObservableList().isEmpty()); // Should still be empty
    }

    @Test
    void addIntervieweeWithAlias_validNameInvalidAlias_exceptionThrownButIntervieweeAdded()
            throws IllegalActionException {
        IntervieweeList interviewees = new IntervieweeList();
        try {
            interviewees.addInterviewee("Bob");
            interviewees.addIntervieweeWithAlias("Tom", "Bob");
            fail();
        } catch (IllegalValueException e) {
            assertEquals("An Interviewee with this name or alias already exists!", e.getMessage());
        }
        assertDoesNotThrow(() -> interviewees.getInterviewee("Tom"));
    }

    @Test
    void setAlias_duplicateName_exceptionThrown() throws IllegalValueException, IllegalActionException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addInterviewee("Bob");
        interviewees.addInterviewee("Tom");
        try {
            interviewees.setAlias("Tom", "Bob");
            fail();
        } catch (IllegalValueException e) {
            assertEquals("An Interviewee with this name or alias already exists!", e.getMessage());
        }
    }

    @Test
    void setAlias_oldAliasDeleted() throws IllegalActionException, IllegalValueException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addIntervieweeWithAlias("Bob Dylan", "Bob");
        interviewees.addIntervieweeWithAlias("Tom Marvolo Riddle", "Riddle");
        interviewees.setAlias("Riddle", "Tom");
        try {
            interviewees.getInterviewee("Riddle");
            fail();
        } catch (IllegalActionException e) {
            assertEquals("No interviewee with the given identifier can be found", e.getMessage());
        }
    }

    @Test
    void setName_oldNameDeleted() throws IllegalActionException, IllegalValueException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addIntervieweeWithAlias("Bob Dylan", "Bob");
        interviewees.addIntervieweeWithAlias("Tom Marvolo Riddle", "Riddle");
        interviewees.setName("Riddle", "Tom Riddle");
        try {
            interviewees.getInterviewee("Tom Marvolo Riddle");
            fail();
        } catch (IllegalActionException e) {
            assertEquals("No interviewee with the given identifier can be found", e.getMessage());
        }
    }

    @Test
    void deleteInterviewee_validIdentifier_intervieweeDeleted() throws IllegalActionException, IllegalValueException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addInterviewee("Bob");
        interviewees.addIntervieweeWithAlias("Tom", "T");
        interviewees.deleteInterviewee("1"); // delete Bob by id
        assertThrows(IllegalActionException.class, () -> interviewees.getInterviewee("Bob"));
        interviewees.deleteInterviewee("T"); // delete Tom by alias
        assertThrows(IllegalActionException.class, () -> interviewees.getInterviewee("Tom"));
    }

    @Test
    void getInterviewee_idAndFullName_sameIntervieweeReturned() throws IllegalValueException, IllegalActionException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addInterviewee("Hello World!");
        Interviewee a = interviewees.getInterviewee("1");
        Interviewee b = interviewees.getInterviewee("Hello World!");
        assertEquals(a, b);
    }

    @Test
    void getObservableList_increasingIdOrder() throws IllegalValueException, IllegalActionException {
        IntervieweeList interviewees = new IntervieweeList();
        interviewees.addInterviewee("A");
        interviewees.addInterviewee("B");
        interviewees.addInterviewee("C");
        interviewees.addInterviewee("D");
        interviewees.addInterviewee("E");
        interviewees.deleteInterviewee("D");
        int previous = 0;
        for (Interviewee i : interviewees.getObservableList()) {
            assertTrue(i.getId() > previous);
            previous = i.getId();
        }
    }
}
