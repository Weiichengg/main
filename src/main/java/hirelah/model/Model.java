package hirelah.model;

import java.nio.file.Path;
import java.util.Optional;

import hirelah.commons.core.GuiSettings;
import hirelah.model.hirelah.AppPhase;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.IntervieweeToScore;
import hirelah.model.hirelah.Metric;
import hirelah.model.hirelah.MetricList;
import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionList;
import hirelah.model.hirelah.Transcript;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import javafx.collections.ObservableList;

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
     * Returns the user prefs' sessions directory.
     */
    Path getSessionsDirectory();

    /**
     * Sets the user prefs' sessions directory.
     */
    void setSessionsDirectory(Path sessionsDirectory);

    /**
     * Returns the directory of the current session.
     */
    Optional<Path> getCurrentSession();

    /**
     * Sets the directory of the current session.
     */
    void setCurrentSession(Path session);

    /**
     * Closes the current session, resetting Model state to {@code PRE_SESSION}, with no current session directory
     * and the model not finalized yet.
     */
    Path closeSession();

    /** Returns an unmodifiable view of the attribute list */
    ObservableList<Attribute> getAttributeListView();

    /** Returns an unmodifiable view of the question list */
    ObservableList<Question> getQuestionListView();

    /** Returns an unmodifiable view of the filtered interviewee list */
    ObservableList<Interviewee> getIntervieweeListView();

    /** Returns an unmodifiable view of the metric list */
    ObservableList<Metric> getMetricListView();

    /** Returns the list of interviewees for the current interview session */
    IntervieweeList getIntervieweeList();

    void setIntervieweeList(IntervieweeList intervieweeList);

    /** Returns the list of attributes to score interviewees by */
    AttributeList getAttributeList();

    void setAttributeList(AttributeList attributeList);

    /** Returns the list of questions to ask during interviews */
    QuestionList getQuestionList();

    void setQuestionList(QuestionList questionList);

    /** Returns the list of metrics that describes certain weight */
    MetricList getMetricList();

    void setMetricList(MetricList metricList);

    /** Returns the list of best N interviewees based on certain measures */
    ObservableList<IntervieweeToScore> getBestNInterviewees();

    /** Sets the current mode of the App */
    void setAppPhase(AppPhase phase);

    /** Returns the current mode of the App */
    AppPhase getAppPhase();

    /**
     * Sets the interviewee currently in focus, either when viewing his/her transcript or
     * when interviewing him/her.
     */
    void setCurrentInterviewee(Interviewee interviewee);

    /** Returns the interviewee currently in focus */
    Interviewee getCurrentInterviewee();

    /** Checks whether there is an interviewee on focus at the current state */
    boolean hasCurrentInterviewee();

    /** Returns the transcript of the current interviewee */
    Transcript getCurrentTranscript();

    /** Starts an interview with the given interviewee. Error if the interviewee has been interviewed. */
    void startInterview(Interviewee interviewee) throws IllegalActionException;

    /** Indicates that the interview has ended */
    void endInterview();

    /** Finalizes the interviewees, questions and attributes so they do not change between interviews */
    void finaliseInterviewProperties();

    /** Checks whether the interviewees, questions and attributes has been finalised */
    boolean isFinalisedInterviewProperties();

}
