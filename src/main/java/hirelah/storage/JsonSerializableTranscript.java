package hirelah.storage;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionList;
import hirelah.model.hirelah.Transcript;
import hirelah.model.hirelah.exceptions.IllegalActionException;

/**
 * A class to represent a Transcript in JSON.
 */
public class JsonSerializableTranscript {
    private final HashMap<String, Double> attributeToScore;
    private final List<JsonAdaptedRemark> remarkList;
    private final Instant startTime;
    private final boolean completed;

    @JsonCreator
    public JsonSerializableTranscript(@JsonProperty("attributeToScore") HashMap<String, Double> attributeToScore ,
                                      @JsonProperty("remarkList") List<JsonAdaptedRemark> remarkList,
                                      @JsonProperty("completed") boolean completed,
                                      @JsonProperty("startTime") Instant startTime) {
        this.attributeToScore = attributeToScore;
        this.remarkList = remarkList;
        this.startTime = startTime;
        this.completed = completed;
    }

    public JsonSerializableTranscript(Transcript transcript) {
        this.attributeToScore = new HashMap<>();
        transcript.getAttributeToScoreData()
                .forEach(attributeDoublePair -> this.attributeToScore.put(attributeDoublePair.getKey().toString(),
                        attributeDoublePair.getValue()));
        this.remarkList = transcript.getRemarkListView().stream()
                .map(JsonAdaptedRemark::new).collect(Collectors.toList());
        this.startTime = transcript.getStartTime();
        this.completed = transcript.isCompleted();
    }

    /**
     * Converts the JsonSerializableTranscript to a Transcript with validation of Questions and Attributes.
     *
     * @param questions the questions of the session.
     * @param attributes the attributes of the session.
     * @return the Transcript restored from json.
     * @throws IllegalValueException if saved Transcript represents an illegal state.
     * @throws IllegalActionException if saved Transcript represents an illegal state.
     */
    public Transcript toModelType(QuestionList questions, AttributeList attributes)
            throws IllegalValueException, IllegalActionException {
        Transcript transcript = new Transcript(questions, attributes, startTime);
        loadRemarks(transcript, questions);
        loadScores(transcript, attributes);
        if (completed) {
            transcript.complete();
        }
        return transcript;
    }

    /** Loads saved remarks into the given Transcript.*/
    private void loadRemarks(Transcript transcript, QuestionList questions)
            throws IllegalValueException, IllegalActionException {
        for (JsonAdaptedRemark remark : remarkList) {
            if (remark.getQuestionNumber() == null) {
                transcript.addRemark(remark.getMessage(), remark.getTime());
            } else {
                Question questionToCheck = questions.find(remark.getQuestionNumber());
                if (questionToCheck.toString().equals(remark.getMessage())) {
                    transcript.startQuestion(remark.getQuestionNumber(), questionToCheck, remark.getTime());
                } else {
                    throw new IllegalValueException("Question in Transcript does not match question list!");
                }
            }
        }
    }

    /** Loads saved scores into the given Transcript. */
    private void loadScores(Transcript transcript, AttributeList attributes) throws IllegalValueException {
        for (Attribute attribute : attributes) {
            if (!attributeToScore.containsKey(attribute.toString())) {
                throw new IllegalValueException("Missing attribute: " + attribute);
            }
            transcript.setAttributeScore(attribute, attributeToScore.get(attribute.toString()));
        }
    }
}
