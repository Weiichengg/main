package hirelah.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import hirelah.commons.core.GuiSettings;
import hirelah.commons.core.LogsCenter;
import hirelah.commons.exceptions.IllegalValueException;
import hirelah.logic.Logic;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.ToggleView;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.logic.parser.exceptions.ParseException;
import hirelah.model.hirelah.Interviewee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private IntervieweeListPanel intervieweeListPanel;
    private BestIntervieweeListPanel bestNIntervieweesPanel;
    private RemarkListPanel remarkListPanel;
    private AttributeListPanel attributeListPanel;
    private MetricListPanel metricListPanel;
    private QuestionListPanel questionListPanel;
    private ResultDisplay resultDisplay;

    // SecondWindow for displaying additional information during interview phase.
    private SecondWindow secondWindow;

    // On startup, HireLah shows the list of interviewees
    private ToggleView toggleView = ToggleView.INTERVIEWEE;


    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane listPanelStackPane;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        attributeListPanel = new AttributeListPanel(logic.getAttributeListView());
        intervieweeListPanel = new IntervieweeListPanel(logic.getIntervieweeListView(), this::executeCommand);
        bestNIntervieweesPanel = new BestIntervieweeListPanel(logic.getBestNIntervieweesView(), this::executeCommand);
        attributeListPanel = new AttributeListPanel(logic.getAttributeListView());
        metricListPanel = new MetricListPanel(logic.getMetricListView());
        questionListPanel = new QuestionListPanel(logic.getQuestionListView());
        secondWindow = new SecondWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        listPanelStackPane.getChildren().add(intervieweeListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getSessionsDirectory());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Sets what is displayed in the listPanelStackPane based on the toggle.
     *
     * @param toggleView enum representing what should be displayed
     */
    public void handleToggle(ToggleView toggleView) {
        // Short circuit if no change to the view, unless currently viewing transcript
        // which may change if it is a different interviewee's transcript
        if (this.toggleView == toggleView && toggleView != ToggleView.TRANSCRIPT) {
            return;
        }
        this.toggleView = toggleView;
        secondWindow.hide();
        // Clear the current interviewee and close SecondWindow if not viewing a report
        if (this.toggleView != ToggleView.TRANSCRIPT) {
            logic.setCurrentInterviewee(null);
        }

        listPanelStackPane.getChildren().clear();
        switch (toggleView) {
        case ATTRIBUTE: // attribute
            listPanelStackPane.getChildren().add(attributeListPanel.getRoot());
            break;
        case INTERVIEWEE: // interviewee
            listPanelStackPane.getChildren().add(intervieweeListPanel.getRoot());
            break;

        case METRIC: // metrics
            listPanelStackPane.getChildren().add(metricListPanel.getRoot());
            break;
        case QUESTION: // questions
            listPanelStackPane.getChildren().add(questionListPanel.getRoot());
            break;
        case TRANSCRIPT: // transcript
            Interviewee currentInterviewee = logic.getCurrentInterviewee();
            DetailedIntervieweeCard detailedIntervieweeCard =
                    new DetailedIntervieweeCard(currentInterviewee, this::executeCommand);
            remarkListPanel = new RemarkListPanel(currentInterviewee, logic.getQuestionListView());
            listPanelStackPane.getChildren().addAll(remarkListPanel.getRoot(), detailedIntervieweeCard.getRoot());
            StackPane.setAlignment(detailedIntervieweeCard.getRoot(), Pos.TOP_CENTER);
            StackPane.setAlignment(remarkListPanel.getRoot(), Pos.CENTER);
            // show second window
            if (!currentInterviewee.getTranscript().get().isCompleted()) {
                secondWindow.show(questionListPanel);
            }

            break;
        case BEST_INTERVIEWEE:
            bestNIntervieweesPanel = new BestIntervieweeListPanel(logic.getBestNIntervieweesView(),
                    this::executeCommand);
            listPanelStackPane.getChildren().add(bestNIntervieweesPanel.getRoot());
            break;
        default:
            break;
        }
    }

    /**
     * Opens the user guide PDF on help command. Adapted from https://stackoverflow.com/questions/15654154.
     */
    public void handleHelp() {
        try {
            String inputPdf = "/help/UserGuide.pdf";
            Path tempOutput = Files.createTempFile("Temp", ".pdf");
            tempOutput.toFile().deleteOnExit();
            System.out.println("tempOutput: " + tempOutput);
            InputStream is = getClass().getResourceAsStream(inputPdf);
            Files.copy(is, tempOutput, StandardCopyOption.REPLACE_EXISTING);
            Desktop.getDesktop().open(tempOutput.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        primaryStage.hide();
        secondWindow.hide();
    }

    /**
     * Sets feedback in the result display.
     *
     * @param feedback the feedback to display.
     */
    public void setFeedbackToUser(String feedback) {
        resultDisplay.setFeedbackToUser(feedback);
    }

    /**
     * Scrolls the Transcript to the given index.
     *
     * @param index the index to scroll to.
     */
    public void scrollTranscriptTo(int index) {
        remarkListPanel.scrollTo(index);
    }


    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private void executeCommand(String commandText) throws CommandException, IllegalValueException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            commandResult.displayResult(this);
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
