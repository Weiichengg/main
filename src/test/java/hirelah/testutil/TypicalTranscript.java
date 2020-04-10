package hirelah.testutil;

import static hirelah.testutil.TypicalAttributes.getTypicalAttributes;
import static hirelah.testutil.TypicalQuestionList.getTypicalQns;

import java.time.Duration;
import java.time.Instant;

import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.Transcript;

/**A utility class containing a list of {@code Transcript}  to be used in tests.*/
public class TypicalTranscript {
    public static final String REMARK1 = "He is very good with JAVA:)";
    public static final String REMARK2 = "He is very okay with python:(";
    public static final Instant NOW = Instant.parse("2007-12-03T10:15:30.00Z");
    public static final Duration TIME1 = Duration.ofMinutes(3);
    public static final Duration TIME2 = Duration.ofMinutes(15);

    public static Transcript getTypicalTranscript() {
        Transcript transcript = new Transcript(getTypicalQns(), getTypicalAttributes(), NOW);
        transcript.addRemark(REMARK1, TIME1);
        transcript.addRemark(REMARK2, TIME2);
        double score = 1.0;
        for (Attribute attribute: getTypicalAttributes()) {
            transcript.setAttributeScore(attribute, score++);
        }
        return transcript;
    }
}
