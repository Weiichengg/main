package hirelah.ui;

import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.model.hirelah.Metric;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of metrics.
 */
public class MetricListPanel extends UiPart<Region> {
    private static final String FXML = "MetricCardListView.fxml";
    private final Logger logger = LogsCenter.getLogger(MetricListPanel.class);

    @FXML
    private ListView<Metric> metricCardListView;

    public MetricListPanel(ObservableList<Metric> metricList) {
        super(FXML);
        metricCardListView.setItems(metricList);
        metricCardListView.setCellFactory(listView -> new MetricListPanel.MetricListViewCell());
        metricCardListView.getItems().addListener(
                 (ListChangeListener<Metric>) c -> metricCardListView.scrollTo(c.getList().size() - 1));
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

