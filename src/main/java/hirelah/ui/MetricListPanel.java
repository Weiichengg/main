package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Metric;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of metrics.
 */
public class MetricListPanel extends UiPart<Region> {
    private static final String FXML = "CardListView.fxml";
    private final Logger logger = LogsCenter.getLogger(MetricListPanel.class);

    @FXML
    private ListView<Metric> cardListView;

    @FXML
    private Label title;

    public MetricListPanel(ObservableList<Metric> metricList) {
        super(FXML);
        title.setText("Metrics");
        cardListView.setItems(metricList);
        cardListView.setCellFactory(listView -> new MetricListPanel.MetricListViewCell());
        // cardListView.getItems().addListener(
        //         (ListChangeListener<Metric>) c -> cardListView.scrollTo(c.getList().size()-1));
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Metric} using a {@code MetricCard}.
     */
    class MetricListViewCell extends ListCell<Metric> {
        @Override
        protected void updateItem(Metric metric, boolean empty) {
            super.updateItem(metric, empty);

            if (empty || metric == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new MetricCard(metric).getRoot());
            }
        }
    }
}

