package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.IntervieweeToScore;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of best n interviewees ranked by a user-defined metric.
 */
public class BestIntervieweeListPanel extends UiPart<Region> {
    private static final String FXML = "IntervieweeListPanel.fxml";

    @FXML
    protected Label title;

    private final Logger logger = LogsCenter.getLogger(BestIntervieweeListPanel.class);
    private final CommandExecutor commandExecutor;

    @FXML
    private ListView<IntervieweeToScore> intervieweeListView;


    public BestIntervieweeListPanel(ObservableList<IntervieweeToScore> intervieweeList,
                                    CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        title.setText("Best Interviewees");
        intervieweeListView.setItems(intervieweeList);
        intervieweeListView.setCellFactory(listView -> new IntervieweeToScoreListViewCell());
        intervieweeListView.getItems().addListener(
                (ListChangeListener<IntervieweeToScore>) c -> intervieweeListView.scrollTo(c.getList().size() - 1));

        //this.getRoot().setOnKeyPressed(key -> {
        //    KeyCode keyCode = key.getCode();
        //    if (keyCode == KeyCode.ENTER) {
        //        try {
        //            commandExecutor.execute("open " + this.interviewee.getFullName());
        //        } catch (CommandException | IllegalValueException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});
        //cardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IntervieweeToScore>() {
        //    @Override
        //    public void changed(
        //            ObservableValue<? extends IntervieweeToScore> observable,
        //            IntervieweeToScore oldValue, IntervieweeToScore newValue) {
        //        try {
        //            commandExecutor.execute("open " + newValue.getInterviewee().getFullName());
        //        } catch (CommandException | IllegalValueException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Interviewee} using a {@code IntervieweeCard}.
     */
    class IntervieweeToScoreListViewCell extends ListCell<IntervieweeToScore> {
        @Override
        protected void updateItem(IntervieweeToScore score, boolean empty) {
            super.updateItem(score, empty);

            if (empty || score == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new IntervieweeCard(score.getInterviewee(), commandExecutor, score.getScore()).getRoot());
            }
        }
    }

}
