package hirelah.model.hirelah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.exceptions.IllegalActionException;

class RemarkListTest {

    @Test
    void getRemarks_retrieveRemarks_success() {
        RemarkList actualRemarks = new RemarkList(3);
        assertTrue(actualRemarks.getRemarks().isEmpty());
    }

    @Test
    void getIndexAtTime_getFirstRemark_success() {
        RemarkList actualRemarks = new RemarkList(3);
        actualRemarks.addRemark(RemarkTest.REMARK_START.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_EARLIER.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_MIDDLE.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_LATER.getMessage());
        int result = actualRemarks.getIndexAtTime(RemarkTest.DEFAULT_START_TIME);
        assertEquals(0, result);
    }

    @Test
    void isQuestionAnswered_noAnswer_success() throws IllegalValueException {
        RemarkList actualRemarks = new RemarkList(3);
        actualRemarks.addRemark(RemarkTest.REMARK_START.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_MIDDLE.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_LATER.getMessage());
        assertFalse(actualRemarks.isQuestionAnswered(3));
    }

    @Test
    void getIndexOfQuestion_firstQuestion_success() throws IllegalActionException, IllegalValueException {
        RemarkList actualRemarks = new RemarkList(3);
        actualRemarks.addRemark(RemarkTest.REMARK_START.getMessage());
        actualRemarks.startQuestion(1, new Question("How are you?"));
        actualRemarks.addRemark(RemarkTest.REMARK_MIDDLE.getMessage());
        actualRemarks.addRemark(RemarkTest.REMARK_LATER.getMessage());
        assertEquals(1, actualRemarks.getIndexOfQuestion(1));
    }
}
