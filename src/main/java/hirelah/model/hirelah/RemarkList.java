package hirelah.model.hirelah;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.IntStream;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A list of remarks that are associated  with a particular interview session of an {@code Interviewee}.
 * Some remarks are associated with a question, as it is part of the answer to a question, and some remarks do not.
 * Both are able to be inserted to this remark list. Once a remark has been added, it cannot be removed from the list.
 * Supports a minimal set of list operations.
 *
 * @see Remark
 */
public class RemarkList {
    private final Instant startTime;
    private final ObservableList<Remark> remarks = FXCollections.observableArrayList();
    private final int[] questionIndices;

    public RemarkList(int questionsCount) {
        this(questionsCount, Instant.now());
    }

    public RemarkList(int questionsCount, Instant startTime) {
        this.questionIndices = new int[questionsCount + 1];
        this.startTime = startTime;
    }

    /**
     * Retrieves the remark list encapsulated by {@code RemarkList}.
     */
    public ObservableList<Remark> getRemarks() {
        return FXCollections.unmodifiableObservableList(remarks);
    }

    /**
     * Adds a remark to the list.
     *
     * @param message The message of the remark to be added to the list.
     */
    public void addRemark(String message) {
        requireNonNull(message);
        remarks.add(new Remark(Duration.between(startTime, Instant.now()), message));
    }

    /**
     * Adds a remark to the list with given time. Used in storage.
     */
    public void addRemark(String message, Duration time) {
        requireNonNull(message);
        remarks.add(new Remark(time, message));
    }

    /**
     * Indicates the beginning of an answer to a question by inserting a QuestionRemark into the list.
     *
     * @param questionNumber the question number.
     * @param question the question being asked.
     */
    public void startQuestion(int questionNumber, Question question)
            throws IllegalActionException, IllegalValueException {
        requireNonNull(question);
        if (isQuestionAnswered(questionNumber)) {
            throw new IllegalActionException("Question is already answered!");
        }
        questionIndices[questionNumber] = remarks.size();
        remarks.add(new QuestionRemark(Duration.between(startTime, Instant.now()), questionNumber, question));
    }

    /**
     * Adds a question remark to the list with given time. Used in storage.
     */
    public void startQuestion(int questionNumber, Question question, Duration time)
            throws IllegalActionException, IllegalValueException {
        requireNonNull(question);
        if (isQuestionAnswered(questionNumber)) {
            throw new IllegalActionException("Question is already answered!");
        }
        questionIndices[questionNumber] = remarks.size();
        remarks.add(new QuestionRemark(time, questionNumber, question));
    }

    /**
     * Retrieves the number of {@code Remark}s in {@code RemarkList}.
     *
     * @return The number of {@code Remark}s in this {@code RemarkList}.
     */
    private int size() {
        return this.remarks.size();
    }


    /**
     * Retrieves index of {@code Remark} created around the given time.
     *
     * @param time Time queried.
     * @return The index of the {@code Remark} at or just after time, or the last index
     *         if time exceeds the interview duration.
     */
    public int getIndexAtTime(Duration time) {
        return IntStream.range(0, size())
                .filter(i -> remarks.get(i).getTime().compareTo(time) >= 0)
                .findFirst()
                .orElse(size() - 1);
    }

    /**
     * Checks if a question is answered during the interview
     * by checking whether there is a {@code Remark} that is associated with it.
     *
     * @param questionNumber Question that is checked against.
     * @return Whether the question has {@code Remark} associated with it.
     */
    public boolean isQuestionAnswered(int questionNumber) throws IllegalValueException {
        try {
            return questionIndices[questionNumber] != 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalValueException(String.format(
                    "There are only %d questions in this interview session.", questionIndices.length - 1)
            );
        }
    }

    /**
     * Retrieves the index of the Remark when this {@code Question}
     * was first asked.
     *
     * @param questionIndex Question number that is queried.
     * @return The index of the {@code Remark} in the RemarkList
     *         that was first associated with this {@code Question}.
     * @throws IllegalActionException If the question queried has not been answered.
     */
    public int getIndexOfQuestion(int questionIndex) throws IllegalActionException, IllegalValueException {
        if (!isQuestionAnswered(questionIndex)) {
            throw new IllegalActionException("This question was not answered!");
        }
        return questionIndices[questionIndex];
    }
    public int getNumbOfQns() {
        return questionIndices.length;
    }

    public Instant getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) { // short circuit if it is the same object
            return true;
        } else if (!(other instanceof RemarkList)) {
            return false;
        }
        RemarkList comparison = (RemarkList) other;
        for (int i = 0; i < questionIndices.length; i++) {
            if (comparison.questionIndices[i] != questionIndices[i]) {
                return false;
            }
        }
        return comparison.startTime.equals(startTime)
                && comparison.remarks.equals(remarks);
    }
    @Override
    public int hashCode() {
        return Objects.hash(startTime, questionIndices , remarks);
    }
}
