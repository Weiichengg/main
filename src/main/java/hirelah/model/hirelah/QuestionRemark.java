package hirelah.model.hirelah;

import java.time.Duration;

/**
 * A marker in the RemarkList to indicate when the questions were answered.
 */
public class QuestionRemark extends Remark {
    private int questionNumber;

    public QuestionRemark(Duration time, int questionNumber, Question question) {
        super(time, question.toString());
        this.questionNumber = questionNumber;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof QuestionRemark
                && this.getTime().equals(((QuestionRemark) other).getTime())
                && this.getMessage().equals(((QuestionRemark) other).getMessage())
                && this.getQuestionNumber() == ((QuestionRemark) other).getQuestionNumber());
    }
}
