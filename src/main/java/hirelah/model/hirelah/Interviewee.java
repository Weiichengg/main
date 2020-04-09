package hirelah.model.hirelah;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import hirelah.commons.exceptions.IllegalValueException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A class to represent Interviewee candidates in a Session. They have
 * <ul>
 *     <li>a unique ID per session.</li>
 *     <li>the candidate's full name.</li>
 *     <li>optionally, an alias to refer to the candidate.</li>
 *     <li>optionally, a resume (a file object).</li>
 *     <li>a Transcript if they have been interviewed.</li>
 * </ul>
 */
public class Interviewee {

    public static final String MESSAGE_CONSTRAINTS =
            "Names and aliases should not be numbers, eg. 12345, and should not be blank";

    private final StringProperty fullName = new SimpleStringProperty(null);
    private final int id;
    private final StringProperty alias = new SimpleStringProperty(null);
    private final ObjectProperty<File> resume = new SimpleObjectProperty<>(null);
    private final ObjectProperty<Transcript> transcript = new SimpleObjectProperty<>(null);

    /**
     * Creates a new Interviewee in the system which starts with no alias, no resume and
     * being not yet interviewed.
     *
     * @param fullName The interviewee's full name.
     * @param id The interviewee's unique interviewee id.
     */
    public Interviewee(String fullName, int id) throws IllegalValueException {
        setFullName(fullName);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName.getValue();
    }

    /**
     * Sets the full name. Allows renaming of Interviewees.
     *
     * @param fullName the new name of the interviewee.
     * @throws IllegalValueException if the alias given is an invalid identifier.
     */
    public void setFullName(String fullName) throws IllegalValueException {
        checkValidIdentifier(fullName);
        this.fullName.setValue(fullName);
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public Optional<String> getAlias() {
        return Optional.ofNullable(alias.getValue());
    }

    /**
     * Sets the alias of the interviewee if absent, or modifies it if present.
     *
     * @param alias The new alias to refer to this interviewee.
     * @throws IllegalValueException if the alias given is an invalid identifier.
     */
    public void setAlias(String alias) throws IllegalValueException {
        checkValidIdentifier(alias);
        this.alias.setValue(alias);
    }

    public StringProperty aliasProperty() {
        return alias;
    }

    public Optional<File> getResume() {
        return Optional.ofNullable(resume.getValue());
    }

    public void setResume(File resume) {
        this.resume.setValue(resume);
    }

    public ObjectProperty<File> resumeProperty() {
        return resume;
    }

    public Optional<Transcript> getTranscript() {
        return Optional.ofNullable(transcript.getValue());
    }

    public void setTranscript(Transcript transcript) {
        assert this.transcript.getValue() == null;
        this.transcript.setValue(transcript);
    }

    public ObjectProperty<Transcript> transcriptProperty() {
        return transcript;
    }

    /**
     * Checks if an interviewee has completed their interview. {@code getScore} can be called if this is true.
     *
     * @return true if the interview has been completed, false otherwise.
     */
    public boolean isInterviewed() {
        return getTranscript().map(Transcript::isCompleted).orElse(false);
    }

    /**
     * Gets the score of an interviewee that has already been interviewed.
     * It is guaranteed that this method is only called on Interviewees that have been interviewed.
     *
     * @param attribute the Attribute to retrieve the score for.
     * @return the score of the given Attribute.
     * @throws NullPointerException if Interviewee has not been interviewed.
     *                              However, it is guaranteed not to occur at runtime
     *                              as the IntervieweeList is filtered before sorting by score.
     */
    public double getScore(Attribute attribute) {
        return transcript.getValue().getAttributeScore(attribute);
    }

    /**
     * Validates a string meant to be used as an alias or the full name to ensure it is
     * not a valid number, and is not the empty string. Any other string will be considered valid,
     * since there are names that contain punctuation, non-ascii characters and even numbers.
     *
     * @param identifier the alias or name to validate.
     * @throws IllegalValueException if identifier is empty, or is a valid number
     */
    private void checkValidIdentifier(String identifier) throws IllegalValueException {
        try {
            Integer.parseUnsignedInt(identifier);

            // If successfully parsed as an integer, reject the identifier
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        } catch (NumberFormatException e) {

            // If identifier only contains whitespace or is an empty string, reject it
            if (identifier.isBlank()) {
                throw new IllegalValueException(MESSAGE_CONSTRAINTS);
            }
        }
    }

    @Override
    public String toString() {
        return getFullName();
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) { // short circuit if it is the same object
            return true;
        } else if (!(other instanceof Interviewee)) {
            return false;
        }
        Interviewee interviewee = (Interviewee) other;
        return interviewee.toString().equals(toString())
                && interviewee.getAlias().equals(getAlias())
                && (interviewee.getId() == getId())
                && interviewee.isInterviewed() == isInterviewed()
                && interviewee.getTranscript().equals(getTranscript())
                && interviewee.getResume().equals(getResume());
    }
    @Override
    public int hashCode() {
        return Objects.hash(fullName, id, alias, resume, transcript);
    }
}
