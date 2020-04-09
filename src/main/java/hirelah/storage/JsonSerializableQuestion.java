package hirelah.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionList;

/**
 * An Immutable QuestionList that is serializable to JSON format {@link QuestionList}.
 */

@JsonRootName(value = "questions")
public class JsonSerializableQuestion {
    public static final String MESSAGE_DUPLICATE_QUESTION = "QUESTION LIST CONTAINS DUPLICATES";
    private final List<JsonAdaptedQuestions> questions = new ArrayList<>();

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    @JsonCreator
    public JsonSerializableQuestion(@JsonProperty("questions") List<JsonAdaptedQuestions> source) {
        this.questions.addAll(source);
    }

    public JsonSerializableQuestion(QuestionList source) {
        List<Question> convertedList = source.getObservableList();
        questions.addAll(convertedList.stream().map(JsonAdaptedQuestions::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public QuestionList toModelType() throws IllegalValueException {
        QuestionList newData = new QuestionList();
        for (JsonAdaptedQuestions jsonAdaptedQuestion : questions) {
            Question question = jsonAdaptedQuestion.toModelType();
            if (newData.isDuplicate(question)) {
                throw new IllegalValueException("This attribute is already exists!");
            }
            newData.add(question.toString()); // temporary only store the name first
        }
        return newData;
    }
}
