package hirelah.model.hirelah;

import static hirelah.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import hirelah.commons.exceptions.IllegalValueException;

public class AttributeListTest {
    @Test
    public void add_success() {
        try {
            new AttributeList().add("courage");
        } catch (IllegalValueException e) {
            fail();
        }
    }

    @Test
    public void add_duplicateAttribute() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.add("courage");
            list.add("courage");
        });
    }

    @Test
    public void find_success() {
        AttributeList list = new AttributeList();
        try {
            list.add("courage");
        } catch (IllegalValueException e) {
            fail();
        }
        try {
            assertEquals(new Attribute("courage"), list.find("cou"));
        } catch (IllegalValueException e) {
            fail();
        }
    }

    @Test
    public void find_emptyAttributeList() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.find("cou");
        });
    }

    @Test
    public void find_attributeNotFound() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.add("tenacity");
            list.find("cou");
        });
    }

    @Test
    public void find_duplicateAttributePrefix() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.add("cooperation");
            list.add("courage");
            list.find("co");
        });
    }

    @Test
    public void delete_success() {
        AttributeList list = new AttributeList();
        try {
            list.add("courage");
        } catch (IllegalValueException e) {
            fail();
        }
        try {
            list.delete("cou");
        } catch (IllegalValueException e) {
            fail();
        }
    }

    @Test
    public void delete_emptyAttributeList() {
        assertThrows(IllegalValueException.class, () -> new AttributeList().delete("cou"));
    }

    @Test
    public void delete_noAttributeFound() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.add("tenacity");
            list.delete("cou");
        });
    }

    @Test
    public void delete_duplicateAttributePrefix() {
        assertThrows(IllegalValueException.class, () -> {
            AttributeList list = new AttributeList();
            list.add("cooperation");
            list.add("courage");
            list.delete("co");
        });
    }
}
