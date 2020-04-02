package seedu.address.ui;

import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.hirelah.Attribute;
import seedu.address.model.hirelah.Interviewee;

/**
 * An UI component that displays information of a {@code Interviewee}.
 */
public class DetailedIntervieweeCard extends UiPart<Region> {

    private static final String FXML = "DetailedIntervieweeCard.fxml";

    public final Interviewee interviewee;
    private final ObservableList<Attribute> attributes;
    private final ObservableList<Double> scores;

    @FXML
    private VBox detailedIntervieweePane;

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private Label alias;

    @FXML
    private Button viewResume;

    @FXML
    private StackPane attributeScore;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private BarChart<String, Double> scoreChart;

    @FXML
    private VBox listContainer;

    @FXML
    private ListView<Attribute> attributeList;

    @FXML
    private ListView<Double> scoreList;

    public DetailedIntervieweeCard(Interviewee interviewee, CommandExecutor commandExecutor) {
        super(FXML);
        this.interviewee = interviewee;
        this.attributes = interviewee.getTranscript().get().getAttributesToBeScored();
        this.scores = interviewee.getTranscript().get().getAttributeScores();
        name.setText(interviewee.getFullName());
        id.setText("ID:         " + interviewee.getId());
        alias.setText("Alias:     " + interviewee.getAlias().orElse("No alias has been set."));
        viewResume.setText(interviewee.getResume().isPresent() ? "View Resume" : "No Resume");

        if (interviewee.getTranscript().get().isCompleted()) {
            attributeScore.getChildren().remove(listContainer);
            initialiseChart(attributes, scores);
        } else {
            attributeScore.getChildren().remove(scoreChart);
            initialiseLists(attributes, scores);
        }


        viewResume.setOnAction(en -> {
            try {
                commandExecutor.execute("resume " + this.interviewee.getFullName());
            } catch (CommandException | IllegalValueException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initialises the ListViews for the attribute scores for this card. A listener is added to the ObservableMap so
     * that the change made by any put operation is reflected in the ListViews.
     */
    private void initialiseLists(ObservableList<Attribute> attributes, ObservableList<Double> scores) {
        // ObservableList<String> attributes = FXCollections.observableArrayList();
        // ObservableList<String> scores = FXCollections.observableArrayList();
        // for (Map.Entry<Attribute, Double> entry : attributeToScoreMapView.entrySet()) {
        //    attributes.add(truncateAttributeName(entry.getKey().toString()));
        //    scores.add(scoreToString(entry.getValue()));
        //}

        attributeList.setItems(attributes);
        attributeList.setCellFactory(listView -> new AttributeToBeScoredViewCell());
        scoreList.setItems(scores);
        scoreList.setCellFactory(listView -> new AttributeScoresViewCell());

        //attributeToScoreMapView.addListener((MapChangeListener<Attribute, Double>) change -> {
        //    if (change.wasAdded()) {
        //        String attributeAdded = truncateAttributeName(change.getKey().toString());
        //        String newScore = scoreToString(change.getValueAdded());
        //        scores.set(attributes.indexOf(attributeAdded), newScore);
        //    }
        //});

    }

    /**
     * Initialises the BarChart for this card.
     */
    @SuppressWarnings("unchecked")
    private void initialiseChart(ObservableList<Attribute> attributes, ObservableList<Double> scores) {
        // ObservableList<XYChart.Data<String, Double>> data = convertMapToList(attributeToScoreMapView);
        ObservableList<XYChart.Data<String, Double>> data = FXCollections.observableArrayList();
        for (Attribute attribute : attributes) {
            data.add(new Data<>(truncateAttributeName(attribute.toString()),
                    scores.get(attributes.indexOf(attribute))));
        }

        for (XYChart.Data<String, Double> bar : data) {
            bar.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {
                        displayLabelForData(bar);
                    }
                }
            });
        }

        XYChart.Series<String, Double> attributeData = new XYChart.Series<>("Attributes", data);

        // setAll method should be safe in our usage but it raises an Unchecked varargs warning
        scoreChart.getData().setAll(attributeData);
        scoreChart.setLegendVisible(false);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(12.4);
        yAxis.setTickUnit(2.5);
        yAxis.setLabel("Scores");

        xAxis.setLabel("Attributes");
        xAxis.setAnimated(false);
        xAxis.setMinWidth(data.size() * 30);
        xAxis.setMaxHeight(20);


    }

    /**
     * Adds a label to the top of every bar, showing the numerical value of the bar.
     * Solution retrieved from https://stackoverflow.com/questions/15237192
     *
     * @param data the Data object representing a bar in the BarChart.
     */
    private void displayLabelForData(XYChart.Data<String, Double> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.2
                        )
                );
            }
        });
    }


    /**
     * Truncates the input xTicks if it is longer than 20 characters.
     *
     * @param xTick String representing a tick on the X axis.
     *
     * @return String of the tick after truncation.
     */
    private String truncateAttributeName(String xTick) {
        if (xTick.length() > 20) {
            return xTick.substring(0, 17) + "...";
        } else {
            return xTick;
        }
    }

    /**
     * Converts and formats a Double score into a String.
     *
     * @param score Double score associated with an attribute.
     * @return String representation of the score.
     */
    private String scoreToString(Double score) {
        if (Double.isNaN(score)) {
            return "-";
        }
        return String.format("%.4f", score);
    }


    /**
     * Converts the ObservableMap of Attribute to Score to an ObservableList of XYChart.Data of type String, Double,
     * used t0 plot a BarChart. A listener is added to the ObservableMap so that the change made by any put operation is
     * reflected in the BarChart.
     *
     * @param mapToScore ObservableMap of Attribute to Score.
     * @return ObservableList XYChart.Data String, Double  used as data input for BarChart.
     */
    private ObservableList<XYChart.Data<String, Double>> convertMapToList(
            ObservableMap<Attribute, Double> mapToScore) {

        ObservableList<XYChart.Data<String, Double>> attributeList = FXCollections.observableArrayList();

        for (Map.Entry<Attribute, Double> entry : mapToScore.entrySet()) {
            attributeList.add(new Data<>(truncateAttributeName(entry.getKey().toString()), entry.getValue()));
        }
        // // No longer necessary as data passed to the Bar Chart is final.
        //mapToScore.addListener((MapChangeListener<Attribute, Double>) change -> {
        //    if (change.wasAdded()) {
        //        String attributeAdded = truncateAttributeName(change.getKey().toString());
        //        for (int i = 0; i < attributeList.size(); i++) {
        //            if (attributeList.get(i).getXValue().equals(attributeAdded)) {
        //                attributeList.remove(i);
        //                break;
        //            }
        //        }
        //        attributeList.add(new Data<>(attributeAdded, change.getValueAdded()));
        //    }
        //});

        return attributeList;
    }


    /**
     * Custom {@code ListCell} that displays the Attribute to be scored.
     */
    class AttributeToBeScoredViewCell extends ListCell<Attribute> {
        @Override
        protected void updateItem(Attribute attribute, boolean empty) {
            super.updateItem(attribute, empty);
            if (empty || attribute == null) {
                setText(null);
            } else {
                setText(truncateAttributeName(attribute.toString()));
            }
            setPrefWidth(100.0);
        }
    }

    /**
     * Custom {@code ListCell} that displays the scores for each attribute.
     */
    class AttributeScoresViewCell extends ListCell<Double> {
        @Override
        protected void updateItem(Double score, boolean empty) {
            super.updateItem(score, empty);
            if (empty || score == null) {
                setText(null);
            } else {
                setText(scoreToString(score));
            }
            setPrefWidth(100.0);
        }
    }



}
