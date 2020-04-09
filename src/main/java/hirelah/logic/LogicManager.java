package hirelah.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import hirelah.commons.core.GuiSettings;
import hirelah.commons.core.LogsCenter;
import hirelah.logic.commands.Command;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.logic.parser.InterviewParser;
import hirelah.logic.parser.NormalParser;
import hirelah.logic.parser.PreSessionParser;
import hirelah.logic.parser.exceptions.ParseException;
import hirelah.model.Model;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeToScore;
import hirelah.model.hirelah.Metric;
import hirelah.model.hirelah.Question;
import hirelah.storage.Storage;
import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);
    private final Model model;
    private final Storage storage;
    private final PreSessionParser preSessionParser;
    private final InterviewParser interviewParser;
    private final NormalParser normalParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        preSessionParser = new PreSessionParser();
        interviewParser = new InterviewParser();
        normalParser = new NormalParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command;
        switch (model.getAppPhase()) {
        case PRE_SESSION:
            command = preSessionParser.parseCommand(commandText);
            break;
        case NORMAL:
            command = normalParser.parseCommand(commandText);
            break;
        case INTERVIEW:
            command = interviewParser.parseCommand(commandText);
            break;
        default:
            throw new IllegalArgumentException("Impossible enum case");
        }
        commandResult = command.execute(model, storage);

        try {
            storage.saveInterviewee(model.getIntervieweeList());
            storage.saveAttribute(model.getAttributeList());
            storage.saveQuestion(model.getQuestionList());
            storage.saveMetric(model.getMetricList());
            storage.saveModel(model.isFinalisedInterviewProperties());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ObservableList<Attribute> getAttributeListView() {
        return model.getAttributeListView();
    }

    @Override
    public ObservableList<Question> getQuestionListView() {
        return model.getQuestionListView();
    }

    @Override
    public ObservableList<Metric> getMetricListView() {
        return model.getMetricListView();
    }

    @Override
    public Interviewee getCurrentInterviewee() {
        return model.getCurrentInterviewee();
    }

    @Override
    public void setCurrentInterviewee(Interviewee interviewee) {
        model.setCurrentInterviewee(interviewee);
    }

    @Override
    public ObservableList<Interviewee> getIntervieweeListView() {
        return model.getIntervieweeListView();
    }

    @Override
    public ObservableList<IntervieweeToScore> getBestNIntervieweesView() {
        return model.getBestNInterviewees();
    }

    @Override
    public Path getSessionsDirectory() {
        return model.getSessionsDirectory();
    }

    /**
     * Returns all the available sessions in the user prefs' sessions directory.
     */
    @Override
    public List<File> getAvailableSessions() throws IOException {
        return storage.readSessions(model.getUserPrefs());
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

}
