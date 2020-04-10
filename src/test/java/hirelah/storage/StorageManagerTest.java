package hirelah.storage;

import static hirelah.testutil.TypicalAttributes.getTypicalAttributes;
import static hirelah.testutil.TypicalInterviewee.getIntervieweeList;
import static hirelah.testutil.TypicalMetricList.getMetricList;
import static hirelah.testutil.TypicalQuestionList.getTypicalQns;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import hirelah.commons.core.GuiSettings;
import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.UserPrefs;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.MetricList;
import hirelah.model.hirelah.QuestionList;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    /**
     * the following is a sample of the
     * AttributeList, Questionlist,
     * MetricList, IntervieweeList and model that will be used
     * for testing of the storage.
     */
    private StorageManager storageManager;
    private boolean model = true;

    /**
     * Setting up of the respective fields
     * needed for the testing.
     */
    @BeforeEach
    public void setUp() throws IllegalValueException {
        /**Initialising the different components of the Storage*/
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        IntervieweeStorage intervieweeStorage = new IntervieweeStorage(getTempFilePath("interviewees"));
        AttributeStorage attributeStorage = new AttributeStorage(getTempFilePath("attributes"));
        QuestionStorage questionStorage = new QuestionStorage(getTempFilePath("questions"));
        MetricStorage metricStorage = new MetricStorage(getTempFilePath("metrics"));
        TranscriptStorage transcriptStorage = new TranscriptStorage(getTempFilePath("transcripts"));
        ModelStorage modelStorage = new ModelStorage(getTempFilePath("model"));
        storageManager = new StorageManager(userPrefsStorage, intervieweeStorage, attributeStorage, questionStorage,
                metricStorage, transcriptStorage, modelStorage);
    }

    /** Provide custom path based on input String. */
    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    /** The following tests check if all the different components of Storage can save and retrieve the
     * various fields accurately.*/
    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void intervieweesReadSave() throws Exception {
        storageManager.saveInterviewee(getIntervieweeList());
        List<Interviewee> data = getIntervieweeList().getObservableList();
        for (int i = 0; i < data.size(); i++) {
            storageManager.saveTranscript(data.get(i));
        }
        IntervieweeList retrieved = storageManager.readInterviewee(getTypicalQns(),
                getTypicalAttributes(), model).get();
        assertEquals(getIntervieweeList(), retrieved);
    }

    @Test
    public void attributesReadSave() throws Exception {
        storageManager.saveAttribute(getTypicalAttributes());
        AttributeList retrieved = storageManager.readAttribute().get();
        assertEquals(getTypicalAttributes(), retrieved);
    }

    @Test
    public void metricsReadSave() throws Exception {
        storageManager.saveMetric(getMetricList());
        MetricList retrieved = storageManager.readMetric().get();
        assertEquals(getMetricList(), retrieved);
    }

    @Test
    public void modelSaveRead() throws Exception {
        storageManager.saveModel(model);
        boolean retrieved = storageManager.readModel().get();
        assertEquals(model, retrieved);
    }

    @Test
    public void qnsSaveRead() throws Exception {
        QuestionList questionList = getTypicalQns();
        storageManager.saveQuestion(questionList);
        QuestionList retrived = storageManager.readQuestion().get();
        assertEquals(questionList, retrived);
    }

    /** The following tests are to check that StorageManager can return
     * the a valid path for the various components.
     */

    @Test
    public void getModelPath() {
        assertNotNull(storageManager.getModelDirectory());
    }

    @Test
    public void getIntervieweePath() {
        assertNotNull(storageManager.getIntervieweeDirectory());
    }

    @Test
    public void getQuestionsPath() {
        assertNotNull(storageManager.getQuestionDirectory());
    }

    @Test
    public void getAttributesPath() {
        assertNotNull(storageManager.getAttributeDirectory());
    }

    @Test
    public void getUserPrefsPath() {
        assertNotNull(storageManager.getUserPrefsFilePath());
    }
}
