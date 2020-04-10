package hirelah.testutil;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.QuestionList;

/**
 * A utility class containing a list of {@code QuestionList}  to be used in tests.
 */
public class TypicalQuestionList {
    public static final String QN1 = "How can you contribute to the company in 5 years?";
    public static final String QN2 = "Are you a good Team-player?";
    public static final String QN3 = "Have you taken CS2103 before?";

    public static QuestionList getTypicalQns() {
        QuestionList typicalQuestions = new QuestionList();
        try {
            typicalQuestions.add(QN1);
            typicalQuestions.add(QN2);
            typicalQuestions.add(QN3);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return typicalQuestions;
    }
}
