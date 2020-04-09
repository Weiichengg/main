package hirelah.model.hirelah;

import hirelah.commons.exceptions.IllegalValueException;

/*
 * Attribute
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 01 Mar 2020
 *
 */

/**
 * <p>Attribute class represents the parameters that are
 * taken into account to assess the interviewee.</p>
 * @author AY1920S2-W15-2
 */
public class Attribute {
    public static final String MESSAGE_CONSTRAINTS =
            "A name of attribute should only contain alphabet characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alpha}][\\p{Alpha} ]*";

    private String name;

    /**
     * Constructs an Attribute instance. Use the static factory method {@link #of(String)} for unvalidated input.
     *
     * @param name The name of the attribute.
     */
    public Attribute(String name) {
        this.name = name;
    }

    /**
     * Constructs an Attribute with validation.
     *
     * @param name The name of the attribute.
     * @return The created attribute.
     * @throws IllegalValueException if the name is invalid.
     */
    public static Attribute of(String name) throws IllegalValueException {
        if (!isValidAttributeName(name)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
        return new Attribute(name);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidAttributeName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Attribute // instanceof handles nulls
                && name.equals(((Attribute) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
