package hirelah.model.hirelah;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import hirelah.commons.exceptions.IllegalValueException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * AttributeList
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 01 Mar 2020
 *
 */

/**
 * <p>AttributeList class manages the list of attributes that have been
 * added by the interviewer. AttributeList can retrieve the attributes using
 * the prefix.</p>
 * @author AY1920S2-W15-2
 */
public class AttributeList implements Iterable<Attribute> {
    private static final String ALREADY_EXISTS = "This attribute is already exists!";
    private static final String DUPLICATE_MESSAGE = "There are multiple attributes with the same prefix.";
    private static final String NOT_FOUND_MESSAGE = "No attributes with the entered name or prefix.";

    private ObservableList<Attribute> attributes;

    /**
     * Constructs an AttributeList instance.
     */
    public AttributeList() {
        this.attributes = FXCollections.observableArrayList();
    }

    public ObservableList<Attribute> getObservableList() {
        return FXCollections.unmodifiableObservableList(attributes);
    }

    /**
     * Adds the attribute to the list.
     *
     * @param attributeName The attribute name.
     * @throws IllegalValueException if the attribute already exists, or the name is invalid
     */
    public void add(String attributeName) throws IllegalValueException {
        Attribute attribute = Attribute.of(attributeName);
        if (isDuplicate(attribute)) {
            throw new IllegalValueException(ALREADY_EXISTS);
        }

        attributes.add(attribute);
    }

    /**
     * Find the attribute based on its full name, then by prefix if no match is found.
     *
     * @param attributePrefix The prefix of the attribute.
     * @return The corresponding Attribute instance.
     * @throws IllegalValueException if the prefix can be multi-interpreted or no such Attribute found.
     */
    public Attribute find(String attributePrefix) throws IllegalValueException {
        Attribute toFind = Attribute.of(attributePrefix); // not inserted, need not validate
        if (attributes.contains(toFind)) {
            return toFind;
        }

        // Match the string as a prefix
        List<Attribute> matches = attributes.stream()
                .filter(attr -> attr.toString().startsWith(attributePrefix))
                .collect(Collectors.toList());

        if (matches.size() > 1) {
            throw new IllegalValueException(DUPLICATE_MESSAGE);
        } else if (matches.isEmpty()) {
            throw new IllegalValueException(NOT_FOUND_MESSAGE);
        } else {
            return matches.get(0);
        }
    }

    /**
     * Edits the attribute based on its full name, then by prefix if no match is found.
     *
     * @param attributePrefix The prefix of the attribute.
     * @param updatedAttribute The name of the updated attribute.
     * @return The edited attribute.
     * @throws IllegalValueException if the prefix can be multi-interpreted or no such Attribute found.
     */
    public Attribute edit(String attributePrefix, String updatedAttribute) throws IllegalValueException {
        Attribute newAttribute = Attribute.of(updatedAttribute);

        if (isDuplicate(newAttribute)) {
            throw new IllegalValueException(ALREADY_EXISTS);
        }
        Attribute currentAttribute = find(attributePrefix);
        int index = attributes.indexOf(currentAttribute);
        attributes.set(index, new Attribute(updatedAttribute));
        return currentAttribute;
    }

    /**
     * Deletes the attribute by its full name, then by prefix if no match is found.
     *
     * @param attributePrefix The prefix of the attribute.
     * @return The deleted attribute
     * @throws IllegalValueException if the prefix can be multi-interpreted or no such Attribute found.
     */
    public Attribute delete(String attributePrefix) throws IllegalValueException {
        Attribute attribute = find(attributePrefix);
        attributes.remove(attribute);
        return attribute;

    }

    public boolean isDuplicate(Attribute attribute) {
        return attributes.contains(attribute);
    }

    @Override
    public Iterator<Attribute> iterator() {
        return attributes.iterator();
    }

    @Override
    public int hashCode() {
        return attributes.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AttributeList // instanceof handles nulls
                && attributes.equals(((AttributeList) other).attributes)); // state check
    }
}
