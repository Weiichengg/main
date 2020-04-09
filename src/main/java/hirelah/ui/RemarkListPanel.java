package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionRemark;
import hirelah.model.hirelah.Remark;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of remarks for an interviewee.
 */
public class RemarkListPanel extends UiPart<Region> {
    private static final String FXML = "RemarkCardListView.fxml";
    private final Logger logger = LogsCenter.getLogger(RemarkListPanel.class);

    private ObservableList<Remark> remarkList;
    private ObservableList<Question> questions;

    @FXML
    private ListView<Remark> remarkCardListView;


    public RemarkListPanel(Interviewee interviewee, ObservableList<Question> questions) {
        super(FXML);
        this.questions = questions;
        if (interviewee.getTranscript().isPresent()) {
            remarkList = interviewee.getTranscript().get().getRemarkListView();
        } else {
            remarkList = null;
        }
        remarkCardListView.setItems(remarkList);
        remarkCardListView.setCellFactory(listView -> new RemarkListViewCell());
        remarkCardListView.getItems().addListener(
            (ListChangeListener<Remark>) c -> {
                remarkCardListView.scrollTo(c.getList().size() - 1);
            });
    }

    public void scrollTo(int index) {
        remarkCardListView.scrollTo(index);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Remark} using a {@code RemarkCard}.
     */
    class RemarkListViewCell extends ListCell<Remark> {
        @Override
        protected void updateItem(Remark remark, boolean empty) {
            super.updateItem(remark, empty);

            if (empty || remark == null) {
                setGraphic(null);
                setText(null);
            } else {
                if (remark instanceof QuestionRemark) {
                    setGraphic(new QuestionRemarkCard(remark, questions).getRoot());
                } else {
                    setGraphic(new RemarkCard(remark).getRoot());
                }
            }
        }
    }
}
