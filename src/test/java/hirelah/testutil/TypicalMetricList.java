package hirelah.testutil;

import static hirelah.testutil.TypicalAttributes.getAttributePrefix;
import static hirelah.testutil.TypicalAttributes.getTypicalAttributes;

import java.util.List;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.MetricList;

/**A utility class containing a list of {@code MetricList}  to be used in tests.*/
public class TypicalMetricList {
    public static final List<Double> WEIGHT1 = List.of(2.0, 0.8, 0.5);
    public static final List<Double> WEIGHT2 = List.of(4.0, 0.3, 0.6);

    public static MetricList getMetricList() {
        MetricList metricList = new MetricList();
        try {
            metricList.add("LeaderShip", getTypicalAttributes(), getAttributePrefix(), WEIGHT1);
            metricList.add("Good Thinker", getTypicalAttributes(), getAttributePrefix(), WEIGHT2);
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
        return metricList;
    }
}
