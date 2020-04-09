//package seedu.address.model.hirelah;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//import static seedu.address.testutil.Assert.assertThrows;
//
//import org.junit.jupiter.api.Test;
//
//import seedu.address.commons.exceptions.IllegalValueException;
//
//public class MetricListTest {
//    @Test
//    public void add_success() {
//        try {
//            assertEquals("Successfully added metric: extremeSkewed", new MetricList().add("extremeSkewed"));
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void add_duplicateMetric() {
//        assertThrows(IllegalValueException.class, () -> {
//            MetricList list = new MetricList();
//            list.add("extremeSkewed");
//            list.add("extremeSkewed");
//        });
//    }
//
//    @Test
//    public void find_success() {
//        MetricList list = new MetricList();
//        try {
//            list.add("extremeSkewed");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//        try {
//            assertEquals(new Metric("extremeSkewed"), list.find("ex"));
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void find_emptyMetricList() {
//        assertThrows(IllegalValueException.class, () -> {
//            MetricList list = new MetricList();
//            list.find("ex");
//        });
//    }
//
//    @Test
//    public void find_attributeNotFound() {
//        assertThrows(IllegalValueException.class, () -> {
//            AttributeList list = new AttributeList();
//            list.add("someWeight");
//            list.find("ex");
//        });
//    }
//
//    @Test
//    public void find_duplicateMetricPrefix() {
//        assertThrows(IllegalValueException.class, () -> {
//            MetricList list = new MetricList();
//            list.add("weightOne");
//            list.add("weightTwo");
//            list.find("wei");
//        });
//    }
//
//    @Test
//    public void delete_success() {
//        MetricList list = new MetricList();
//        try {
//            list.add("extremeSkewed");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//        try {
//            assertEquals("Successfully removed metric: extremeSkewed", list.delete("ex"));
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void delete_emptyMetricList() {
//        assertThrows(IllegalValueException.class, () -> new MetricList().delete("ex"));
//    }
//
//    @Test
//    public void delete_noMetricFound() {
//        assertThrows(IllegalValueException.class, () -> {
//            MetricList list = new MetricList();
//            list.add("weightOne");
//            list.delete("ex");
//        });
//    }
//
//    @Test
//    public void delete_duplicateAttributePrefix() {
//        assertThrows(IllegalValueException.class, () -> {
//            MetricList list = new MetricList();
//            list.add("weightOne");
//            list.add("weightTwo");
//            list.delete("we");
//        });
//    }
//}
