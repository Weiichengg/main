package seedu.address.model.hirelah;

import java.time.Instant;

/**
 * Stores the remark message that are inserted by the interviewer
 * at any given time of the interview. It can be a part of an answer to
 * a particular question, or just a normal remark.
 */
public class Remark {
    private final Instant time;
    private final String message;
    private final Question question;

    /**
     * Constructs a {@code Remark} which is part of the answer
     * to a question of the interview.
     *
     * @param time The time when the remark was made.
     * @param message The remark message.
     * @param question The question associated with this remark as its answer.
     */
    public Remark(Instant time, String message, Question question) {
        this.time = time;
        this.message = message;
        this.question = question;
    }

    /**
     * Constructs a {@code Remark} which is not part of the answer
     * to any question of the interview.
     *
     * @param time The time when the remark was made.
     * @param message The remark message.
     */
    Remark(Instant time, String message) {
        this.time = time;
        this.message = message;
        this.question = null;
    }

    /**
     * Retrieves the Instant where this {@code Remark}
     * was created.
     *
     * @return  The Instant when this {@code Remark} was created.
     */
    public Instant getTime() {
        return time;
    }

    /**
     * Retrieves the Question associated with this {@code Remark}
     * if there is any.
     *
     * @return The Question associated with this {@code Remark} if there is any.
     */
    public Question getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return this.message;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Remark) {
            Remark otherRemark = ((Remark) other);
            if (question == null || otherRemark.getQuestion() == null) {
                return question == otherRemark.getQuestion();
            } else {
                return time.equals(otherRemark.time)
                        && message.equals(otherRemark.message)
                        && question.equals(otherRemark.question);
            }
        } else {
            return false;
        }
    }
}
