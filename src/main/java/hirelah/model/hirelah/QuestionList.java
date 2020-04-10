package hirelah.model.hirelah;

import hirelah.commons.exceptions.IllegalValueException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * QuestionList
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 02 Mar 2020
 *
 */

/**
 * <p>QuestionList class manages the list of questions that
 * have been added by the interviewer. Questions can be retrieved
 * by their ordering.</p>
 * @author AY1920S2-W15-2
 */
public class QuestionList {
    private static final String ALREADY_EXISTS = "This question is already exists!";
    private ObservableList<Question> questions;

    public QuestionList() {
        this.questions = FXCollections.observableArrayList();
    }

    public ObservableList<Question> getObservableList() {
        return FXCollections.unmodifiableObservableList(questions);
    }

    /**
     * Adds the question to the list, if the input is valid.
     *
     * @param questionDescription The question.
     * @throws IllegalValueException If the question already exists.
     */
    public void add(String questionDescription) throws IllegalValueException {
        Question question = new Question(questionDescription);
        if (isDuplicate(question)) {
            throw new IllegalValueException(ALREADY_EXISTS);
        }

        questions.add(question);
    }

    /**
     * Returns the number of questions.
     *
     * @return the number of questions in QuestionList.
     */
    public int size() {
        return questions.size();
    }

    /**
     * Finds the question based on its index, if the index
     * string entered is a valid integer string.
     * @param index The index of the question.
     * @return The corresponding Question instance.
     * @throws IllegalValueException If the questionIndex is not a number or the index is out of bound.
     */
    public Question find(int index) throws IllegalValueException {
        if (index > questions.size() || index <= 0) {
            throw new IllegalValueException("No such question exists!");
        }
        return questions.get(index - 1);
    }

    /**
     * Deletes the question based on its index, if the index
     * string entered is a valid integer string.
     *
     * @param index The index that wants to be deleted.
     * @return The deleted question.
     * @throws IllegalValueException If the questionIndex is not a number or the index is out of bound.
     */
    public Question delete(int index) throws IllegalValueException {
        if (index > questions.size() || index <= 0) {
            throw new IllegalValueException("No such question exists!");
        }
        Question question = questions.get(index - 1);
        questions.remove(index - 1);
        return question;
    }

    /**
     * Edits the question based on its index with a new description,
     * if the index string entered is a valid integer string.
     *
     * @param index The index of the question that wants to be edited.
     * @param description The updated description of the question.
     * @return The corresponding question.
     * @throws IllegalValueException If the questionIndex is not a number or the index is out of bound.
     */

    public Question edit(int index, String description) throws IllegalValueException {
        if (index > questions.size() || index <= 0) {
            throw new IllegalValueException("No such question exists!");
        }

        Question newQuestion = new Question(description);

        if (isDuplicate(newQuestion)) {
            throw new IllegalValueException(ALREADY_EXISTS);
        }
        Question current = questions.get(index - 1);
        Question question = new Question(description);
        questions.set(index - 1, question);
        return current;
    }

    public boolean isDuplicate(Question question) {
        return questions.stream().anyMatch(question::equals);
    }

    @Override
    public int hashCode() {
        return questions.hashCode();
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof QuestionList // instanceof handles nulls
                && questions.equals(((QuestionList) other).questions)); // state check
    }
}
