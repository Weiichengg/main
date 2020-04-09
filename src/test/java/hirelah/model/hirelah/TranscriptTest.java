package hirelah.model.hirelah;

import static hirelah.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.exceptions.IllegalActionException;

class TranscriptTest {
    Transcript getTranscript() throws IllegalValueException {
        QuestionList questions = new QuestionList();
        questions.add("One");
        questions.add("Two");
        AttributeList attributes = new AttributeList();
        attributes.add("Attribute one");
        attributes.add("Attribute two");
        return new Transcript(questions, attributes);
    }

    @Test
    void getIndexAtTime_beyondLastTime_returnLastIndex() throws IllegalValueException {
        Transcript actualTranscript = getTranscript();
        actualTranscript.addRemark(RemarkTest.REMARK_START.getMessage());
        actualTranscript.addRemark(RemarkTest.REMARK_EARLIER.getMessage());
        assertEquals(1, actualTranscript.getIndexAtTime(Duration.ofSeconds(10000)));
    }

    @Test
    void getTranscriptAtQuestion_unansweredQuestion_failure() throws IllegalValueException {
        Transcript actualTranscript = getTranscript();
        assertThrows(IllegalActionException.class, () ->
                actualTranscript.getIndexOfQuestion(1));
    }

    @Test
    void getTranscriptAtQuestion_answeredQuestion_success() throws IllegalActionException, IllegalValueException {
        Transcript actualTranscript = getTranscript();
        actualTranscript.addRemark(RemarkTest.REMARK_START.getMessage());
        actualTranscript.startQuestion(1, new Question("What's your name?"));
        actualTranscript.addRemark(RemarkTest.REMARK_LATER.getMessage());
        assertEquals(1, actualTranscript.getIndexOfQuestion(1));
    }

}
