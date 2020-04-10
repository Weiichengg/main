package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Question;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of questions.
 */
public class QuestionListPanel extends UiPart<Region> {
    private static final String FXML = "TextListView.fxml";
    private final Logger logger = LogsCenter.getLogger(QuestionListPanel.class);

    @FXML
    private ListView<Question> textListView;

    public QuestionListPanel(ObservableList<Question> questionList) {
        super(FXML);
        textListView.setItems(questionList);
        textListView.setCellFactory(listView -> new QuestionListViewCell());
        textListView.getItems().addListener(
                (ListChangeListener<Question>) c -> textListView.scrollTo(c.getList().size() - 1));
    }


    /**
     * Custom {@code ListCell} that displays the Question text with the question number.
     */
    class QuestionListViewCell extends ListCell<Question> {
        @Override
        protected void updateItem(Question question, boolean empty) {
            super.updateItem(question, empty);
            int questionNumber = getIndex() + 1;
            setText(question == null ? "" : questionNumber + ". " + question.toString());
            setPrefWidth(150.0);
            setWrapText(true);
        }
    }
}
