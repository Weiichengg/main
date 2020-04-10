package hirelah.ui;

import java.util.Map;

import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.Metric;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;


/**
 * An UI component that displays information of an {@code Interviewee}.
 */
public class MetricCard extends UiPart<Region> {

    private static final String FXML = "MetricListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Metric metric;

    @FXML
    private BorderPane metricCardPane;

    @FXML
    private Label metricName;

    @FXML
    private TableView<Map.Entry<Attribute, Double>> attributeToWeight;

    /**
     * Constructs a metric card which shows the weight assigned to each attribute for the interview.
     *
     * @param metric Metric object to take information from.
     */
    public MetricCard(Metric metric) {
        super(FXML);
        this.metric = metric;
        metricName.setText(this.metric.getName());

        ObservableList<Map.Entry<Attribute, Double>> items = FXCollections.observableArrayList(metric
                .getMap()
                .entrySet());

        attributeToWeight.setItems(items);

        TableColumn<Map.Entry<Attribute, Double>, Attribute> attributeColumn = new TableColumn<>("Attribute:");
        attributeColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getKey()));

        attributeToWeight.getColumns().set(0, attributeColumn);

        TableColumn<Map.Entry<Attribute, Double>, Double> weightColumn = new TableColumn<>("Weight:");
        weightColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));

        attributeToWeight.getColumns().set(1, weightColumn);

        attributeColumn.prefWidthProperty().bind(attributeToWeight.widthProperty().multiply(0.48));
        weightColumn.prefWidthProperty().bind(attributeToWeight.widthProperty().multiply(0.41));

        attributeColumn.setResizable(false);
        weightColumn.setResizable(false);
        attributeToWeight.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

}
