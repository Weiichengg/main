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
//public class QuestionListTest {
//    @Test
//    public void add_success() {
//        try {
//            new QuestionList().add("what is this question?");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void add_duplicateQuestion() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.add("what is this question?");
//        });
//    }
//
//    @Test
//    public void find_success() {
//        QuestionList list = new QuestionList();
//        try {
//            list.add("what is this question?");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//        try {
//            assertEquals(new Question("what is this question?"), list.find("1"));
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void find_inputNotANumber() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.find("wha");
//        });
//    }
//
//    @Test
//    public void find_inputNegativeIndex() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.find("-1");
//        });
//    }
//
//    @Test
//    public void find_inputOutOfBound() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.find("5");
//        });
//    }
//
//    @Test
//    public void find_emptyList() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.find("1");
//        });
//    }
//
//    @Test
//    public void delete_success() {
//        QuestionList list = new QuestionList();
//        try {
//            list.add("what is this question?");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//        try {
//            list.delete("1");
//        } catch (IllegalValueException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void delete_emptyList() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.delete("1");
//        });
//    }
//
//    @Test
//    public void delete_inputNotANumber() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.delete("what");
//        });
//    }
//
//    @Test
//    public void delete_inputNegativeIndex() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.delete("-1");
//        });
//    }
//
//    @Test
//    public void delete_inputOutOfBound() {
//        assertThrows(IllegalValueException.class, () -> {
//            QuestionList list = new QuestionList();
//            list.add("what is this question?");
//            list.delete("5");
//        });
//    }
//}
