package seedu.address.model.hirelah.storage;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.Metric;

/**
 * Jackson-friendly version of {@link Metric}.
 */
public class JsonAdaptedMetric {
    private String name;
    private HashMap<Attribute, Double> attributeToWeight;
    // storing of the hashmap will be done by the subsequent commit
    /**
     * Constructs a {@code JsonAdaptedMetric} with the the relevant details.
     */
    @JsonCreator
    public JsonAdaptedMetric(Metric source) {
        this.name = source.toString();
        this.attributeToWeight = null; //temporary
    }
    /**
     * Converts this Jackson-friendly adapted metric object into the model's {@code Metric} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted metric.
     */
    public Metric toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException("Invalid name for the matric");
        }
        return new Metric(name, attributeToWeight); // do not contain the recording, alias and interview session
    }

}
