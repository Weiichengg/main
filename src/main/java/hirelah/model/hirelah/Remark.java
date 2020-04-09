package hirelah.model.hirelah;

import java.time.Duration;

/**
 * Stores the remark message that are inserted by the interviewer
 * at any given time of the interview. It can be a part of an answer to
 * a particular question, or just a normal remark.
 */
public class Remark {
    private final Duration time;
    private final String message;

    /**
     * Constructs a {@code Remark} with the given message at given time.
     *
     * @param time The time when the remark was made.
     * @param message The remark message.
     */
    public Remark(Duration time, String message) {
        this.time = time;
        this.message = message;
    }


    /**
     * Retrieves the time of the interview when this {@code Remark}
     * was created.
     *
     * @return  The Duration since interview start when this {@code Remark} was created.
     */
    public Duration getTime() {
        return time;
    }

    /**
     * Formats the time in a readable format.
     *
     * @return the formatted time string in minutes and seconds.
     */
    public String getTimeString() {
        return String.format("%d:%02d", time.toMinutes(), time.toSecondsPart());
    }

    public String getMessage() {
        return message;
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
            return time.equals(otherRemark.getTime())
                    && message.equals(otherRemark.getMessage());
        } else {
            return false;
        }
    }
}
