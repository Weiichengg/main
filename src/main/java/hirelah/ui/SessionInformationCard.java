package hirelah.ui;

import java.nio.file.Path;

import hirelah.model.hirelah.Interviewee;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;


/**
 * An UI component that displays meta information of an Interview Session.
 */
public class SessionInformationCard extends UiPart<Region> {

    private static final String FXML = "SessionInformationCard.fxml";

    @FXML
    private Label sessionName;

    @FXML
    private Label interviews;

    @FXML
    private Label finaliseStatus;

    public SessionInformationCard(
            Path currentSession, ObservableList<Interviewee> intervieweeList, boolean isFinalised) {
        super(FXML);
        sessionName.setText("Session: " + currentSession.getFileName().toString());
        int numberInterviewees = 0;
        int numberInterviewed = 0;
        for (Interviewee interviewee : intervieweeList) {
            numberInterviewees++;
            if (interviewee.isInterviewed()) {
                numberInterviewed++;
            }
        }
        interviews.setText(String.format("  - %d / %d interviewed", numberInterviewed, numberInterviewees));
        finaliseStatus.setText(isFinalised ? "  - Rubrics Finalised" : "  - Rubrics NOT Finalised");

    }

}
