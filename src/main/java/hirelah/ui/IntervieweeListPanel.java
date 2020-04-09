package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Interviewee;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of interviewees.
 */
public class IntervieweeListPanel extends UiPart<Region> {
    private static final String FXML = "CardListView.fxml";

    @FXML
    protected Label title;

    private final Logger logger = LogsCenter.getLogger(IntervieweeListPanel.class);
    private final CommandExecutor commandExecutor;

    @FXML
    private ListView<Interviewee> cardListView;


    public IntervieweeListPanel(ObservableList<Interviewee> intervieweeList, CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        title.setText("Interviewees");
        cardListView.setItems(intervieweeList);
        cardListView.setCellFactory(listView -> new IntervieweeListViewCell());
        cardListView.getItems().addListener(
                (ListChangeListener<Interviewee>) c -> cardListView.scrollTo(c.getList().size() - 1));

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
        //cardListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Interviewee>() {
        //    @Override
        //    public void changed(
        //            ObservableValue<? extends Interviewee> observable, Interviewee oldValue, Interviewee newValue) {
        //        try {
        //                commandExecutor.execute("open " + newValue.getFullName());
        //            } catch (CommandException | IllegalValueException e) {
        //                e.printStackTrace();
        //            }
        //    }
        //});

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Interviewee} using a {@code IntervieweeCard}.
     */
    class IntervieweeListViewCell extends ListCell<Interviewee> {
        @Override
        protected void updateItem(Interviewee interviewee, boolean empty) {
            super.updateItem(interviewee, empty);

            if (empty || interviewee == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new IntervieweeCard(interviewee, commandExecutor).getRoot());
            }
        }
    }

}
