package hirelah.testutil;

import java.util.List;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.AttributeList;

/**A utility class containing a list of {@code AttributeList}  to be used in tests.*/
public class TypicalAttributes {
    public static final String ATTRIBUTE1 = "Leadership";
    public static final String ATTRIBUTE2 = "Teamplayer";
    public static final String ATTRIBUTE3 = "Productivity";
    public static final List<String> PREFIX = List.of("Leader", "Team", "Produc");

    public static AttributeList getTypicalAttributes() {
        AttributeList attributes = new AttributeList();
        try {
            attributes.add(ATTRIBUTE1);
            attributes.add(ATTRIBUTE2);
            attributes.add(ATTRIBUTE3);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    public static List<String> getAttributePrefix() {
        return PREFIX;
    }
}
