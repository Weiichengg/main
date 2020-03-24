package seedu.address.model.hirelah.storage;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.hirelah.Interviewee;
import seedu.address.model.hirelah.Transcript;

/**
 * Jackson-friendly version of {@link Interviewee}.
 */
class JsonAdaptedInterviewee {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private String fullname;
    private final int id;
    private Optional<String> alias;
    private Optional<File> resume;
    private Optional<Transcript> interview;

    /**
     * Constructs a {@code JsonAdaptedInterviewee} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedInterviewee(Interviewee source) {
        fullname = source.getFullName();
        id = source.getId();
        alias = source.getAlias();
        resume = source.getResume();
        interview = source.getTranscript();
    }
    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Interviewee} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */

    public Interviewee toModelType() throws IllegalValueException {
        if (fullname == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Name"));
        }
        if (Integer.valueOf(id) == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "ID"));
        }
        return new Interviewee(fullname, id); // do not contain the recording, alias and interview session
    }
}
