package hirelah.ui;

import hirelah.model.hirelah.Question;
import hirelah.model.hirelah.QuestionRemark;
import hirelah.model.hirelah.Remark;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * An UI component that displays information of an {@code Remark}.
 */
public class QuestionRemarkCard extends UiPart<Region> {

    private static final String FXML = "QuestionRemarkCard.fxml";

    public final QuestionRemark questionRemark;

    private Question question;

    @FXML
    private VBox remarkCardPane;
    @FXML
    private Label questionHeader;
    @FXML
    private Label underline;

    /**
     * Constructs a question remark header in a RemarkList.
     *
     * @param remark remark object to take information from.
     */
    public QuestionRemarkCard(Remark remark, ObservableList<Question> questions) {
        super(FXML);
        this.questionRemark = (QuestionRemark) remark;
        question = questions.get(questionRemark.getQuestionNumber() - 1);
        String questionText;
        if (question.toString().length() < 22) {
            questionText = question.toString();
        } else {
            questionText = question.toString().substring(0, 20) + "...";
        }
        questionHeader.setText("Question " + questionRemark.getQuestionNumber() + ": " + questionText);
    }
}
