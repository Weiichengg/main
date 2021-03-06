package seedu.address.model.hirelah.storage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.Interviewee;
import seedu.address.model.hirelah.QuestionList;
import seedu.address.model.hirelah.Transcript;

/**
 * Jackson-friendly version of {@link Interviewee}.
 */
class JsonAdaptedInterviewee {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private String fullName;
    private final Integer id;
    private String alias;
    private String resume;
    private final boolean transcript;


    /**
     * Constructs a {@code JsonAdaptedInterviewee} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedInterviewee(@JsonProperty("fullName") String fullName,
                                  @JsonProperty("id") int id, @JsonProperty("alias") String alias,
                                  @JsonProperty("resume") String resume,
                                  @JsonProperty("transcript") boolean transcript) {
        this.fullName = fullName;
        this.id = id;
        this.alias = alias;
        this.resume = resume;
        this.transcript = transcript;
    }
    public JsonAdaptedInterviewee(Interviewee source) {
        fullName = source.getFullName();
        id = source.getId();
        alias = source.getAlias().orElse(null);
        resume = source.getResume().map(File::getAbsolutePath).orElse(null);
        transcript = source.getTranscript().isPresent();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Interviewee} object.
     *
     * @return the stored Interviewee.
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Interviewee toModelType(QuestionList questionList, AttributeList attributeList,
                                   TranscriptStorage storage, Boolean finalised)
            throws IllegalValueException, DataConversionException {
        Interviewee interviewee = new Interviewee(fullName, id);
        if (transcript) {
            String location = storage.toString() + id.toString() + ".json";
            Path path = Paths.get(location);
            Optional<Transcript> transcript = storage.readTranscript(path, questionList, attributeList);
            if (transcript.isEmpty() || finalised) {
                throw new IllegalValueException("There is is an error in loading the transcript for " + fullName);
            }
            interviewee.setTranscript(transcript.get());
        }

        if (alias != null) {
            interviewee.setAlias(alias);
        }

        return interviewee;
    }
}
