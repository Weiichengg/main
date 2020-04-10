package hirelah.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.logic.Logic;
import hirelah.logic.commands.ToggleView;
import hirelah.model.hirelah.Interviewee;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;




/**
 * InterviewPanel which contains all the information regarding an interview session.
 */
public class InterviewPanel extends UiPart<Region> {
    private static final String FXML = "InterviewPanel.fxml";
    private static final String PRO_TIP = "Pro Tip:\n"
            + "      Start by setting some interview rubrics (questions, \n"
            + "        attributes, metrics) to evaluate interviewees. \n"
            + "      Type \"interview <interviewee>\" to start an interview.\n"
            + "      Type \"help\" to get help.";

    private final Logger logger = LogsCenter.getLogger(InterviewPanel.class);
    private final CommandExecutor commandExecutor;
    private final Logic logic;

    // Independent Ui parts residing in this Ui container
    private IntervieweeListPanel intervieweeListPanel;
    private AttributeListPanel attributeListPanel;
    private MetricListPanel metricListPanel;
    private QuestionListPanel questionListPanel;
    private RemarkListPanel remarkListPanel;
    private SessionInformationCard sessionInformationCard;

    @FXML
    private AnchorPane intervieweePane;

    @FXML
    private AnchorPane transcriptPane;
    @FXML
    private Label emptyTranscriptPaneLabel;

    @FXML
    private TabPane rubricsPane;
    @FXML
    private Tab attributesTab;
    @FXML
    private Tab questionsTab;
    @FXML
    private Tab metricsTab;

    public InterviewPanel(Logic logic, CommandExecutor commandExecutor) {
        super(FXML);
        this.logic = logic;
        this.commandExecutor = commandExecutor;

        fillInnerParts();

    }

    /**
     * Fills up all the placeholders of this window.
     */
    private void fillInnerParts() {
        intervieweeListPanel = new IntervieweeListPanel(logic.getIntervieweeListView(), commandExecutor);
        setAnchor(intervieweeListPanel.getRoot(), 0.0, 0.0, 5.0, 5.0);
        intervieweePane.getChildren().add(intervieweeListPanel.getRoot());
        requireNonNull(logic.getCurrentSession());
        sessionInformationCard = new SessionInformationCard(logic.getCurrentSession().get(),
                logic.getIntervieweeListView(),
                logic.isFinalisedInterviewProperties());
        setAnchor(sessionInformationCard.getRoot(), 0.0, 0.0, 5.0, Double.NaN);
        intervieweePane.getChildren().add(sessionInformationCard.getRoot());

        emptyTranscriptPaneLabel.setText(PRO_TIP);

        setAnchor(rubricsPane, 0.0, 0.0, 3.0, 0.0);
        attributeListPanel = new AttributeListPanel(logic.getAttributeListView());
        attributesTab.setContent(attributeListPanel.getRoot());
        questionListPanel = new QuestionListPanel(logic.getQuestionListView());
        questionsTab.setContent(questionListPanel.getRoot());
        metricListPanel = new MetricListPanel(logic.getMetricListView());
        metricsTab.setContent(metricListPanel.getRoot());
    }

    /**
     * Sets what is displayed in the listPanelStackPane based on the toggle.
     *
     * @param toggleView enum representing what should be displayed
     */
    public void handleToggle(ToggleView toggleView) {
        // Clear the current interviewee
        if (toggleView != ToggleView.TRANSCRIPT) {
            logic.setCurrentInterviewee(null);
        }
        switch (toggleView) {
        case INTERVIEWEE: // interviewee
            intervieweePane.getChildren().clear();
            intervieweePane.getChildren().add(intervieweeListPanel.getRoot());
            requireNonNull(logic.getCurrentSession());
            sessionInformationCard = new SessionInformationCard(logic.getCurrentSession().get(),
                    logic.getIntervieweeListView(),
                    logic.isFinalisedInterviewProperties());
            setAnchor(sessionInformationCard.getRoot(), 0.0, 0.0, 5.0, Double.NaN);
            intervieweePane.getChildren().add(sessionInformationCard.getRoot());
            break;
        case BEST_INTERVIEWEE:
            intervieweePane.getChildren().clear();
            BestIntervieweeListPanel bestNIntervieweesPanel =
                    new BestIntervieweeListPanel(logic.getBestNIntervieweesView(), commandExecutor);
            setAnchor(bestNIntervieweesPanel.getRoot(), 0.0, Double.NaN, 5.0, 5.0);
            intervieweePane.getChildren().add(bestNIntervieweesPanel.getRoot());
            intervieweePane.getChildren().add(sessionInformationCard.getRoot());
            break;
        case TRANSCRIPT: // transcript
            transcriptPane.getChildren().clear();
            Interviewee currentInterviewee = logic.getCurrentInterviewee();
            DetailedIntervieweeCard detailedIntervieweeCard =
                    new DetailedIntervieweeCard(currentInterviewee, commandExecutor);
            remarkListPanel = new RemarkListPanel(currentInterviewee, logic.getQuestionListView());
            transcriptPane.getChildren().add(remarkListPanel.getRoot());
            setAnchor(remarkListPanel.getRoot(), 5.0, 0.0, 5.0, 5.0);
            transcriptPane.getChildren().add(detailedIntervieweeCard.getRoot());
            setAnchor(detailedIntervieweeCard.getRoot(), Double.NaN, 0.0, Double.NaN, 5.0);
            break;
        case ATTRIBUTE: // attribute
            rubricsPane.getSelectionModel().select(attributesTab);
            break;
        case METRIC: // metrics
            rubricsPane.getSelectionModel().select(metricsTab);
            break;
        case QUESTION: // questions
            rubricsPane.getSelectionModel().select(questionsTab);
            break;

        default:
            break;
        }
    }

    /**
     * Scrolls the Transcript to the given index.
     *
     * @param index the index to scroll to.
     */
    public void scrollTo(int index) {
        remarkListPanel.scrollTo(index);
    }

    /**
     * Sets surrounding anchors for a Node if the Node is added to an AnchorPane.
     *
     * @param n Node of interest.
     * @param left Double value of left anchor. If value = Double.NaN, anchor is not set.
     * @param top Double value of top anchor. If value = Double.NaN, anchor is not set.
     * @param right Double value of right anchor. If value = Double.NaN, anchor is not set.
     * @param bottom Double value of bottom anchor. If value = Double.NaN, anchor is not set.
     */
    private void setAnchor(Node n, Double left, Double top, Double right, Double bottom) {
        if (!left.isNaN()) {
            AnchorPane.setLeftAnchor(n, left);
        }
        if (!right.isNaN()) {
            AnchorPane.setRightAnchor(n, right);
        }
        if (!top.isNaN()) {
            AnchorPane.setTopAnchor(n, top);
        }
        if (!bottom.isNaN()) {
            AnchorPane.setBottomAnchor(n, bottom);
        }
    }

}

