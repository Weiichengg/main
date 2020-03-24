package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
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
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.Question;
import seedu.address.model.hirelah.QuestionList;
import seedu.address.model.hirelah.Transcript;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private AppPhase appPhase;
    private final IntervieweeList intervieweeList;
    private final AttributeList attributeList;
    private final QuestionList questionList;
    private final MetricList metricList;
    private final UserPrefs userPrefs;
    private boolean finalisedInterviewProperties;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(userPrefs);

        logger.fine("Initializing with user prefs " + userPrefs);

        this.appPhase = AppPhase.PRE_SESSION;

        this.intervieweeList = new IntervieweeList();
        this.attributeList = new AttributeList();
        this.questionList = new QuestionList();
        this.metricList = new MetricList();
        this.userPrefs = new UserPrefs(userPrefs);
    }

    public ModelManager() {
        this(new UserPrefs());
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

    //=========== Observable accessors =============================================================


    @Override
    public ObservableList<Attribute> getAttributeListView() {
        return FXCollections.unmodifiableObservableList(attributeList.getObservableList());
    }

    @Override
    public ObservableList<Question> getQuestionListView() {
        return FXCollections.unmodifiableObservableList(questionList.getObservableList());
    }

    @Override
    public ObservableList<Transcript> getTranscriptListView(Interviewee interviewee) {
        return FXCollections.observableList(List.of());
    }

    @Override
    public ObservableList<Interviewee> getFilteredIntervieweeListView() {
        return FXCollections.unmodifiableObservableList(intervieweeList.getObservableList());
    }

    //=========== Model component accessors ========================================================

    @Override
    public IntervieweeList getIntervieweeList() {
        return intervieweeList;
    }

    /**
     * Returns the list of attributes to score interviewees by
     */
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

    /**
     * Finalizes the questions and attributes so they do not change between interviews.
     */

    public void finaliseInterviewProperties() {
        this.finalisedInterviewProperties = true;
    }

    /** Checks whether the interviewees, questions and attributes has been finalised */
    @Override
    public boolean isfinalisedInterviewProperties() {
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
