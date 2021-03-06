package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.hirelah.AttributeList;
import seedu.address.model.hirelah.IntervieweeList;
import seedu.address.model.hirelah.MetricList;
import seedu.address.model.hirelah.QuestionList;
import seedu.address.model.hirelah.storage.AttributeStorage;
import seedu.address.model.hirelah.storage.IntervieweeStorage;
import seedu.address.model.hirelah.storage.MetricStorage;
import seedu.address.model.hirelah.storage.ModelStorage;
import seedu.address.model.hirelah.storage.QuestionStorage;
import seedu.address.model.hirelah.storage.Storage;
import seedu.address.model.hirelah.storage.StorageManager;
import seedu.address.model.hirelah.storage.TranscriptStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AddressBook ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        IntervieweeStorage intervieweeStorage = new IntervieweeStorage(userPrefs.getIntervieweeDirectory());
        QuestionStorage questionStorage = new QuestionStorage(userPrefs.getQuestionDirectory());
        AttributeStorage attributeStorage = new AttributeStorage(userPrefs.getAttributeDirectory());
        MetricStorage metricStorage = new MetricStorage(userPrefs.getMetricDirectory());
        TranscriptStorage transcriptStorage = new TranscriptStorage(userPrefs.getTranscriptDirectory());
        ModelStorage modelStorage = new ModelStorage(userPrefs.getModelDirectory());
        storage = new StorageManager(userPrefsStorage, intervieweeStorage, attributeStorage,
                questionStorage, metricStorage, transcriptStorage, modelStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with {@code userPrefs}. Components of the model (IntervieweeList, etc.) start
     * empty and are populated once a session is loaded.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<IntervieweeList> intervieweeListOptional;
        Optional<AttributeList> attributeListOptional;
        Optional<QuestionList> questionListOptional;
        Optional<MetricList> metricListOptional;
        Optional<Boolean> modelOptional;

        IntervieweeList initialInterviewees;
        AttributeList initialAttributes;
        QuestionList initialQuestions;
        MetricList initialMetrics;
        Boolean initialModel;

        try {
            attributeListOptional = storage.readAttribute();
            questionListOptional = storage.readQuestion();
            modelOptional = storage.readModel();
            if (modelOptional.isEmpty()) {
                logger.info("model data file not found. Will be starting with an empty model file");
            }
            if (attributeListOptional.isEmpty()) {
                logger.info("Attributes data file not found. Will be starting with an empty attribute file");
            }
            if (questionListOptional.isEmpty()) {
                logger.info("Question data file not found. Will be starting with an empty question file");
            }

            initialModel = modelOptional.orElse(false);
            initialAttributes = attributeListOptional.orElse(new AttributeList());
            initialQuestions = questionListOptional.orElse(new QuestionList());

            // when the the model is not finalised, it will not initialise metric and transcript
            if (!initialModel) {
                initialMetrics = new MetricList();
            } else {
                metricListOptional = storage.readMetric();
                initialMetrics = metricListOptional.orElse(new MetricList());
            }
            intervieweeListOptional = storage.readInterviewee(initialQuestions, initialAttributes, initialModel);
            initialInterviewees = intervieweeListOptional.orElse(new IntervieweeList());

            if (intervieweeListOptional.isEmpty()) {
                logger.info("Interviewees data file not found. Will be starting with an empty interviewee file");
            }
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty Interview Session");
            initialInterviewees = new IntervieweeList();
            initialAttributes = new AttributeList();
            initialQuestions = new QuestionList();
            initialMetrics = new MetricList();
            initialModel = false;
        }

        return new ModelManager(userPrefs, initialInterviewees, initialAttributes,
                initialQuestions, initialMetrics, initialModel);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
