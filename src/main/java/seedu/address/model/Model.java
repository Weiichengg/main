package seedu.address.model;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.hirelah.AppPhase;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.Interviewee;
import seedu.address.model.hirelah.IntervieweeList;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.Question;
import seedu.address.model.hirelah.QuestionList;
import seedu.address.model.hirelah.Transcript;

/**
 * The API of the Model component.
 */
public interface Model {

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getSessionsDirectory();

    /**
     * Sets the user prefs' address book file path.
     */
    void setSessionsDirectory(Path sessionsDirectory);

    /** Returns an unmodifiable view of the attribute list */
    ObservableList<Attribute> getAttributeListView();

    /** Returns an unmodifiable view of the question list */
    ObservableList<Question> getQuestionListView();

    /** Returns an unmodifiable view of the transcript list of an interviewee */
    ObservableList<Transcript> getTranscriptListView(Interviewee interviewee);

    /** Returns an unmodifiable view of the filtered interviewee list */
    ObservableList<Interviewee> getFilteredIntervieweeListView();

    /** Returns the list of interviewees for the current interview session */
    IntervieweeList getIntervieweeList();

    /** Returns the list of attributes to score interviewees by */
    AttributeList getAttributeList();

    /** Returns the list of questions to ask during interviews */
    QuestionList getQuestionList();

    /** Returns the list of metrics that describes certain weight */
    MetricList getMetricList();

    /** Sets the current mode of the App */
    void setAppPhase(AppPhase phase);

    /** Returns the current mode of the App */
    AppPhase getAppPhase();

    /** Finalizes the interviewees, questions and attributes so they do not change between interviews */
    void finaliseInterviewProperties();

    /** Checks whether the interviewees, questions and attributes has been finalised */
    boolean isfinalisedInterviewProperties();
}
