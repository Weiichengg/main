package hirelah.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import hirelah.commons.exceptions.DataConversionException;
import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.QuestionList;

/**
 * An Immutable InterviewList that is serializable to JSON format {@link IntervieweeList}.
 */
@JsonRootName(value = "interviewees")
class JsonSerializableInterviewee {
    private final int uniqueIntervieweeId;
    private final List<JsonAdaptedInterviewee> interviewees;

    /**
     * used to deserialise the interviewee
     * object from json file to java object.
     * @param interviewees list of object
     */
    @JsonCreator
    public JsonSerializableInterviewee(@JsonProperty("uniqueIntervieweeId") int uniqueIntervieweeId,
                                       @JsonProperty("interviewees") List<JsonAdaptedInterviewee> interviewees) {
        this.uniqueIntervieweeId = uniqueIntervieweeId;
        this.interviewees = interviewees;
    }

    public JsonSerializableInterviewee(IntervieweeList source) {
        this.uniqueIntervieweeId = source.getUniqueIntervieweeId();
        List<Interviewee> convertedList = source.getObservableList();
        this.interviewees = new ArrayList<>();
        interviewees.addAll(convertedList.stream().map(JsonAdaptedInterviewee::new).collect(Collectors.toList()));
    }

    /**
     * Converts into an IntervieweeList
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public IntervieweeList toModelType(QuestionList questionList, AttributeList attributeList,
                                       TranscriptStorage storage, Boolean finalised)
            throws DataConversionException, IllegalValueException {
        List<Interviewee> storedInterviewees = new ArrayList<>();
        for (JsonAdaptedInterviewee jsonInterviewee : interviewees) {
            storedInterviewees.add(jsonInterviewee.toModelType(questionList, attributeList, storage, finalised));
        }
        return IntervieweeList.fromList(uniqueIntervieweeId, storedInterviewees);
    }
}
