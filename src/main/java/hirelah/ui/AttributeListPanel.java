package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Attribute;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    @FXML
    private Label title;

    public AttributeListPanel(ObservableList<Attribute> attributeList) {
        super(FXML);
        title.setText("Attributes");
        textListView.setItems(attributeList);
        // Below solution adapted from https://stackoverflow.com/questions/31992698/
        textListView.getItems().addListener(
                (ListChangeListener<Attribute>) c -> textListView.scrollTo(c.getList().size() - 1));
    }

}
