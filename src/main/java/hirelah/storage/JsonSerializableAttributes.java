package hirelah.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.AttributeList;

/**
 * An Immutable JsonAdaptedAttributes that is serializable to JSON format {@link JsonAdaptedAttributes}.
 */
@JsonRootName(value = "attributes")
public class JsonSerializableAttributes {
    private final List<JsonAdaptedAttributes> attributes = new ArrayList<>();
    /**
     * Converts a given {@code AttributeList} into this class for Jackson use.
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    @JsonCreator
    public JsonSerializableAttributes(@JsonProperty("attributes") List<JsonAdaptedAttributes> source) {
        this.attributes.addAll(source);
    }

    public JsonSerializableAttributes(AttributeList source) {
        List<Attribute> convertedList = source.getObservableList();
        attributes.addAll(convertedList.stream().map(JsonAdaptedAttributes::new).collect(Collectors.toList()));
    }
    /**
     * Converts into the model's {@code AttributeList} object.
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AttributeList toModelType() throws IllegalValueException {
        AttributeList newData = new AttributeList();
        for (JsonAdaptedAttributes jsonAdaptedattributes : attributes) {
            Attribute attribute = jsonAdaptedattributes.toModelType();
            if (newData.isDuplicate(attribute)) {
                throw new IllegalValueException("This attribute is already exists!");
            }
            newData.add(attribute.toString());
        }
        return newData;
    }
}
