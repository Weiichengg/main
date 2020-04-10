package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Attribute;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of attributes.
 */
public class AttributeListPanel extends UiPart<Region> {
    private static final String FXML = "TextListView.fxml";
    private final Logger logger = LogsCenter.getLogger(AttributeListPanel.class);

    @FXML
    private ListView<Attribute> textListView;

    public AttributeListPanel(ObservableList<Attribute> attributeList) {
        super(FXML);
        textListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Attribute attribute, boolean empty) {
                super.updateItem(attribute, empty);
                if (empty || attribute == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    int questionNumber = getIndex() + 1;
                    setText(attribute.toString());
                    setPrefWidth(150.0);
                    setWrapText(true);
                }
            }
        });
        textListView.setItems(attributeList);
        // Below solution adapted from https://stackoverflow.com/questions/31992698/
        textListView.getItems().addListener(
                (ListChangeListener<Attribute>) c -> textListView.scrollTo(c.getList().size() - 1));
    }

}
