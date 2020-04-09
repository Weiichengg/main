package hirelah.model.hirelah;

/*
 * Question
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 02 Mar 2020
 *
 */

/**
 * <p>Question class represents the questions that wants to be
 * asked to the interviewee.</p>
 * @author AY1920S2-W15-2
 */

public class Question {
    private String description;

    /**
     * Constructs a Question instance.
     * @param description The description of the question.
     */

    public Question(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Question // instanceof handles nulls
                && description.equals(((Question) other).description)); // state check
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
