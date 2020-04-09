package hirelah.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.commons.exceptions.DataConversionException;
import hirelah.commons.util.StringUtil;
import hirelah.model.Model;
import hirelah.model.ReadOnlyUserPrefs;
import hirelah.model.UserPrefs;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.MetricList;
import hirelah.model.hirelah.QuestionList;

/**
 * Manages storage of different Sessions data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);

    private UserPrefsStorage userPrefsStorage;
    private IntervieweeStorage intervieweeStorage;
    private AttributeStorage attributeStorage;
    private QuestionStorage questionStorage;
    private MetricStorage metricStorage;
    private TranscriptStorage transcriptStorage;
    private ModelStorage modelStorage;

    public StorageManager(UserPrefsStorage userPrefsStorage) {
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(UserPrefsStorage userPrefsStorage, IntervieweeStorage intervieweeStorage,
                          AttributeStorage attributeStorage, QuestionStorage questionStorage,
                          MetricStorage metricStorage, TranscriptStorage transcriptStorage,
                          ModelStorage modelStorage) {
        this.userPrefsStorage = userPrefsStorage;
        this.intervieweeStorage = intervieweeStorage;
        this.attributeStorage = attributeStorage;
        this.questionStorage = questionStorage;
        this.metricStorage = metricStorage;
        this.transcriptStorage = transcriptStorage;
        this.modelStorage = modelStorage;
    }

    // ================ Session methods ==============================
    @Override
    public List<File> readSessions(ReadOnlyUserPrefs userPrefs) throws IOException {
        try {
            Files.createDirectories(userPrefs.getSessionsDirectory());
        } catch (IOException e) {
            logger.severe("Unable to create sessions directory : " + StringUtil.getDetails(e));
        }
        File[] sessions = userPrefs.getSessionsDirectory().toFile().listFiles(File::isDirectory);
        if (sessions == null) {
            throw new IOException("Unable to read sessions directory");
        }
        return Arrays.asList(sessions);
    }

    @Override
    public void loadSession(Model model, Path session) throws DataConversionException {
        this.intervieweeStorage = new IntervieweeStorage(session.resolve("interviewee.json"));
        this.attributeStorage = new AttributeStorage(session.resolve("attribute.json"));
        this.questionStorage = new QuestionStorage(session.resolve("question.json"));
        this.metricStorage = new MetricStorage(session.resolve("metric.json"));
        this.transcriptStorage = new TranscriptStorage(session.resolve("transcript"));
        this.modelStorage = new ModelStorage(session.resolve("model.json"));

        initModelManager(model);
    }

    /** Sets the initial lists inside the model. */
    private void initModelManager(Model model) throws DataConversionException {
        AttributeList initialAttributes = initializeAttribute();
        QuestionList initialQuestions = initializeQuestion();
        Boolean initialModel = initializeModel();
        MetricList initialMetrics = initializeMetric();
        IntervieweeList initialInterviewees = initializeInterviewee(initialQuestions, initialAttributes, initialModel);

        model.setAttributeList(initialAttributes);
        model.setQuestionList(initialQuestions);
        if (initialModel) {
            model.finaliseInterviewProperties();
        }
        model.setMetricList(initialMetrics);
        model.setIntervieweeList(initialInterviewees);
    }


    // ================ InterviewStorage methods ==============================
    /** Save all the IntervieweeList into their Json file*/
    @Override
    public void saveInterviewee(IntervieweeList source) throws IOException {
        logger.fine("Attempting to write to Interviewee data file: " + getIntervieweeDirectory());
        intervieweeStorage.saveInterview(source);
    }

    @Override
    public Optional<IntervieweeList> readInterviewee(QuestionList questionList,
                                                     AttributeList attributeList,
                                                     Boolean initialModel) throws DataConversionException {
        return readInterviewee(intervieweeStorage.getPath(), questionList, attributeList, initialModel);
    }

    /** Reads the Json file and converts them to Interviewee objects*/
    public Optional<IntervieweeList> readInterviewee(Path filepath, QuestionList questionList,
                                                     AttributeList attributeList,
                                                     Boolean initialModel) throws DataConversionException {
        logger.fine("Attempting to read data from Interviewee file: " + filepath);
        return intervieweeStorage
                .readInterviewee(filepath, questionList, attributeList, initialModel, this.transcriptStorage);
    }

    /** Gets the initial interviewee list for a session. */
    private IntervieweeList initializeInterviewee(QuestionList questionList,
                                                  AttributeList attributeList,
                                                  Boolean initialModel) throws DataConversionException {
        Optional<IntervieweeList> intervieweeListOptional = readInterviewee(questionList, attributeList, initialModel);
        if (intervieweeListOptional.isEmpty()) {
            logger.info("Interviewees data file not found. Will be starting with an empty interviewee file");
        }
        return intervieweeListOptional.orElse(new IntervieweeList());
    }

    public Path getIntervieweeDirectory() {
        return intervieweeStorage.getPath();
    }

    // ================ AttributeStorage methods ==============================
    /** Save all the AttributeList into their Json file*/
    @Override
    public void saveAttribute(AttributeList source) throws IOException {
        logger.fine("Attempting to write to Attribute data file: " + getAttributeDirectory());
        attributeStorage.saveAttributes(source);
    }

    @Override
    public Optional<AttributeList> readAttribute() throws DataConversionException {
        return readAttribute(attributeStorage.getPath());
    }

    /** Reads the Json file and converts them to Interviewee objects*/
    public Optional<AttributeList> readAttribute(Path filepath) throws DataConversionException {
        logger.fine("Attempting to read data from Attribute file: " + filepath);
        return attributeStorage.readAttribute(filepath);
    }

    /** Gets the initial attribute list for the session. */
    private AttributeList initializeAttribute() throws DataConversionException {
        Optional<AttributeList> attributeListOptional = readAttribute();
        if (attributeListOptional.isEmpty()) {
            logger.info("Attributes data file not found. Will be starting with an empty attribute file");
        }
        return attributeListOptional.orElse(new AttributeList());
    }

    public Path getAttributeDirectory() {
        return attributeStorage.getPath();
    }

    // ================ QuestionStorage methods ==============================
    /** Save all the QuestionList into their Json file*/
    @Override
    public void saveQuestion(QuestionList source) throws IOException {
        logger.fine("Attempting to write to Metric data file: " + getQuestionDirectory());
        questionStorage.saveQuestions(source);
    }

    @Override
    public Optional<QuestionList> readQuestion() throws DataConversionException {
        return readQuestion(questionStorage.getPath());
    }

    /** Reads the Json file and converts them to Interviewee objects*/
    public Optional<QuestionList> readQuestion(Path filepath) throws DataConversionException {
        logger.fine("Attempting to read data from Question file: " + filepath);
        return questionStorage.readQuestion(filepath);
    }

    /** Gets the initial question list for the session. */
    private QuestionList initializeQuestion() throws DataConversionException {
        Optional<QuestionList> questionListOptional = readQuestion();
        if (questionListOptional.isEmpty()) {
            logger.info("Question data file not found. Will be starting with an empty question file");
        }
        return questionListOptional.orElse(new QuestionList());
    }

    public Path getQuestionDirectory() {
        return questionStorage.getPath();
    }

    // ================ MetricStorage methods ================================
    /** Save all the MetricList into their Json file*/
    @Override
    public void saveMetric(MetricList source) throws IOException {
        logger.fine("Attempting to write to Metric data file: " + getMetricDirectory());
        metricStorage.saveMetrics(source);
    }

    @Override
    public Optional<MetricList> readMetric() throws DataConversionException {
        return readMetric(metricStorage.getPath());
    }

    /** Reads the Json file and converts them to Interviewee objects */
    public Optional<MetricList> readMetric(Path filepath) throws DataConversionException {
        logger.fine("Attempting to read data from Metric file: " + filepath);
        return metricStorage.readMetric(filepath);
    }

    /** Gets the initial metric list for the session. */
    private MetricList initializeMetric() throws DataConversionException {
        Optional<MetricList> metricListOptional = readMetric();
        if (metricListOptional.isEmpty()) {
            logger.info("Metric data file not found. Will be starting with an empty metric file");
        }
        return metricListOptional.orElse(new MetricList());
    }

    public Path getMetricDirectory() {
        return metricStorage.getPath();
    }

    // ================ TranscriptStorage methods ================================
    /** Save the Transcript into their Json file */
    @Override
    public void saveTranscript(Interviewee source) throws IOException {
        logger.fine("Attempting to write to Transcript data file: " + getMetricDirectory());
        transcriptStorage.saveTranscript(source);
    }

    // ================ ModelStorage methods ================================
    public Path getModelDirectory() {
        return modelStorage.getPath();
    }

    /**
     * Saves the Model state to hard disk.
     *
     * @param model the model state - whether the model is finalised.
     * @throws IOException if an error occurs while writing the file.
     */
    @Override
    public void saveModel(Boolean model) throws IOException {
        logger.fine("Attempting to write to Model data file: " + getModelDirectory());
        modelStorage.saveModel(model);
    }

    @Override
    public Optional<Boolean> readModel() throws DataConversionException {
        return readModel(getModelDirectory());
    }

    /**
     * Reads the Model finalised state from memory.
     */
    public Optional<Boolean> readModel(Path filePath) throws DataConversionException {
        logger.fine("Attempting to read data from Model file: " + filePath);
        return modelStorage.readModel(filePath);
    }

    /** Gets the initial finalised status for the session. */
    private Boolean initializeModel() throws DataConversionException {
        Optional<Boolean> modelOptional = readModel();
        if (modelOptional.isEmpty()) {
            logger.info("Model data file not found. Will be starting with an empty model file");
        }
        return modelOptional.orElse(false);
    }

    // ================ UserPrefsStorage methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }
}
