package hirelah.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Attribute;

/**
 * Jackson-friendly version of {@link Attribute}.
 */

class JsonAdaptedAttributes {
    private final String attribute;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedAttributes(@JsonProperty("attribute") String attribute) {
        this.attribute = attribute;
    }

    public JsonAdaptedAttributes(Attribute attribute) {
        this.attribute = attribute.toString();
    }
    /**
     * Converts this Jackson-friendly adapted Attribute object into the model's {@code Attribute} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted attribute.
     */
    public Attribute toModelType() throws IllegalValueException {
        if (attribute == null) {
            throw new IllegalValueException("Invalid Attribute.");
        }
        return new Attribute(attribute);
    }
}
