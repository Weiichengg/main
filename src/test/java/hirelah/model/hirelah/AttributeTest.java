package hirelah.model.hirelah;

import static hirelah.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;

/*
 * AttributeTest
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 01 Mar 2020
 *
 */

/**
 * <p>AttributeTest class tests the methods implemented in
 * the Attribute class.</p>
 * @author AY1920S2-W15-2
 */

public class AttributeTest {
    @Test
    public void of_invalidAttributeName_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () -> Attribute.of("1234"));
    }

    @Test
    public void isValidAttributeName() {
        // invalid name
        assertFalse(Attribute.isValidAttributeName("^")); // only non-alphanumeric characters
        assertFalse(Attribute.isValidAttributeName("curiosity*")); // contains non-alphanumeric characters
        assertFalse(Attribute.isValidAttributeName("12345")); // numbers only
        assertFalse(Attribute.isValidAttributeName("toughness 2")); // alphanumeric characters
        assertFalse(Attribute.isValidAttributeName("Mario Lorenzo Jr the 2nd")); // long names

        // valid name
        assertTrue(Attribute.isValidAttributeName("toughness")); // alphabets only
        assertTrue(Attribute.isValidAttributeName("Mario Lorenzo")); // with capital letters

    }
}
