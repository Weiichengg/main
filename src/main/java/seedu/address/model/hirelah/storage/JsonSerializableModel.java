package seedu.address.model.hirelah.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.hirelah.Interviewee;

/**
 * An Immutable Model that is serializable to JSON format {@link Model}.
 */
@JsonRootName(value = "Model")
class JsonSerializableModel {
    public static final String MESSAGE_DUPLICATE_INTERVIEWEE = "Interviewee list contains duplicate";
    private final List<JsonAdaptedInterviewee> interviewees = new ArrayList<>();
    private final List<JsonAdaptedAttributes> attributes = new ArrayList<>();
    private final List<JsonAdaptedQuestions> questions = new ArrayList<>();
    private final List<JsonAdaptedMetric> metrics = new ArrayList<>();

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableModel(Model source) {
        questions.addAll(source.getQuestionListView().stream()
                .map(JsonAdaptedQuestions::new).collect(Collectors.toList()));
        attributes.addAll(source.getAttributeListView().stream()
                .map(JsonAdaptedAttributes::new).collect(Collectors.toList()));
        interviewees.addAll(source.getFilteredIntervieweeListView().stream()
                .map(JsonAdaptedInterviewee::new).collect(Collectors.toList()));
        metrics.addAll(source.getMetricList().getObservableList().stream()
                .map(JsonAdaptedMetric::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Model toModelType() throws IllegalValueException {
        Model newModel = new ModelManager();
        for (JsonAdaptedInterviewee jsonAdaptedInterviewee : interviewees) {
            Interviewee person = jsonAdaptedInterviewee.toModelType();
            //  addressBook.addPerson(person);
        }
        // Will update this once we confirm the implementation of Model.
        return newModel;
    }
}
