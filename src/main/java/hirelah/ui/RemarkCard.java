package hirelah.ui;

import hirelah.model.hirelah.Remark;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * An UI component that displays information of an {@code Remark}.
 */
public class RemarkCard extends UiPart<Region> {

    private static final String FXML = "RemarkCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Remark remark;

    @FXML
    private VBox remarkCardPane;
    @FXML
    private Label timestamp;
    @FXML
    private Label content;



    /**
     * Constructs a remark card based on the data stored in a Remark object. Implementation may change?
     *
     * @param remark remark object to take information from.
     */
    public RemarkCard(Remark remark) {
        super(FXML);
        this.remark = remark;
        timestamp.setText(remark.getTimeString());
        content.setText(remark.getMessage());
        this.getRoot().prefWidthProperty().bind(remarkCardPane.widthProperty().subtract(30));

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCard)) {
            return false;
        }

        // state check
        RemarkCard card = (RemarkCard) other;
        return timestamp.getText().equals(card.timestamp.getText())
                && content.getText().equals(card.content.getText());
    }

}
