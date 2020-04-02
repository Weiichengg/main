package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.hirelah.AppPhase;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.Interviewee;
import seedu.address.model.hirelah.IntervieweeList;
import seedu.address.model.hirelah.Metric;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.Question;
import seedu.address.model.hirelah.QuestionList;
import seedu.address.model.hirelah.Transcript;
import seedu.address.model.hirelah.exceptions.IllegalActionException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private boolean finalisedInterviewProperties;
    private AppPhase appPhase;
    private Interviewee currentInterviewee;
    private final IntervieweeList intervieweeList;
    private final AttributeList attributeList;
    private final QuestionList questionList;
    private final MetricList metricList;
    private final UserPrefs userPrefs;
    private ObservableList<Interviewee> bestNIntervieweeList;
    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(userPrefs);

        logger.fine("Initializing with user prefs " + userPrefs);

        this.appPhase = AppPhase.NORMAL;

        this.intervieweeList = new IntervieweeList();
        this.attributeList = new AttributeList();
        this.questionList = new QuestionList();
        this.metricList = new MetricList();
        this.userPrefs = new UserPrefs(userPrefs);
        this.bestNIntervieweeList = FXCollections.observableArrayList();
    }

    public ModelManager() {
        this(new UserPrefs());
    }

    public ModelManager(ReadOnlyUserPrefs userPrefs, IntervieweeList initialInterviewees,
                        AttributeList initialAttributes, QuestionList initialQuestions,
                        MetricList initialMetrics, Boolean finalised) {
        logger.fine("Initializing with user prefs " + userPrefs);

        this.appPhase = AppPhase.NORMAL;
        this.finalisedInterviewProperties = finalised;
        this.userPrefs = new UserPrefs(userPrefs);
        this.intervieweeList = initialInterviewees;
        this.attributeList = initialAttributes;
        this.questionList = initialQuestions;
        this.metricList = initialMetrics;
        this.bestNIntervieweeList = FXCollections.observableArrayList();
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getSessionsDirectory() {
        return userPrefs.getSessionsDirectory();
    }

    @Override
    public void setSessionsDirectory(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setSessionsDirectory(addressBookFilePath);
    }

    //=========== App state setters/getters ======================================================

    @Override
    public void setAppPhase(AppPhase phase) {
        this.appPhase = phase;
    }

    /**
     * Returns the current mode of the App
     */
    @Override
    public AppPhase getAppPhase() {
        return appPhase;
    }

    /**
     * Sets the interviewee currently in focus, either when viewing his/her transcript or
     * when interviewing him/her.
     *
     * @param interviewee the interviewee in focus.
     */
    @Override
    public void setCurrentInterviewee(Interviewee interviewee) {
        this.currentInterviewee = interviewee;
    }

    @Override
    public Interviewee getCurrentInterviewee() {
        return currentInterviewee;
    }

    /**
     * Checks whether there is an interviewee currently in focus
     *
     * @return boolean whether there is an interviewee in focus.
     */
    @Override
    public boolean hasCurrentInterviewee() {
        return !(this.currentInterviewee == null);
    }

    /**
     * A utility to get the Transcript of the current Interviewee (guaranteed to exist).
     * Do not use when there is no interviewee.
     */
    @Override
    public Transcript getCurrentTranscript() {
        return currentInterviewee.getTranscript().get();
    }

    @Override
    public void startInterview(Interviewee interviewee) throws IllegalActionException {
        if (interviewee.isInterviewed()) {
            throw new IllegalActionException("Interviewee has been interviewed already!");
        }
        setCurrentInterviewee(interviewee);
        if (currentInterviewee.getTranscript().isEmpty()) {
            currentInterviewee.setTranscript(new Transcript(questionList, attributeList));
        }
        setAppPhase(AppPhase.INTERVIEW);
    }

    @Override
    public void endInterview() {
        setCurrentInterviewee(null);
        setAppPhase(AppPhase.NORMAL);
    }

    //=========== Observable accessors =============================================================


    @Override
    public ObservableList<Attribute> getAttributeListView() {
        return attributeList.getObservableList();
    }

    @Override
    public ObservableList<Question> getQuestionListView() {
        return questionList.getObservableList();
    }

    @Override
    public ObservableList<Interviewee> getFilteredIntervieweeListView() {
        return intervieweeList.getObservableList();
    }

    @Override
    public ObservableList<Metric> getMetricListView() {
        return metricList.getObservableList();
    }

    //=========== Model component accessors ========================================================

    @Override
    public IntervieweeList getIntervieweeList() {
        return intervieweeList;
    }

    @Override
    public AttributeList getAttributeList() {
        return attributeList;
    }

    @Override
    public QuestionList getQuestionList() {
        return questionList;
    }

    @Override
    public MetricList getMetricList() {
        return metricList;
    }

    @Override
    public ObservableList<Interviewee> getBestNInterviewees() {
        return bestNIntervieweeList;
    }

    /**
     * Finalizes the questions and attributes so they do not change between interviews.
     */
    public void finaliseInterviewProperties() {
        this.finalisedInterviewProperties = true;
    }

    /** Checks whether the questions and attributes has been finalised */
    @Override
    public boolean isFinalisedInterviewProperties() {
        return this.finalisedInterviewProperties;
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return userPrefs.equals(other.userPrefs);
    }


}
