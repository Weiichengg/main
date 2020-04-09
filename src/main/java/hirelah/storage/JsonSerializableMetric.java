package hirelah.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Metric;
import hirelah.model.hirelah.MetricList;

/**
 * An Immutable JsonAdaptedMetrics that is serializable to JSON format {@link JsonAdaptedMetric}.
 */
@JsonRootName(value = "metrics")
public class JsonSerializableMetric {
    private final List<JsonAdaptedMetric> metrics = new ArrayList<>();
    /**
     * Converts a given {@code MetricList} into this class for Jackson use.
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    @JsonCreator
    public JsonSerializableMetric(@JsonProperty("metrics") List<JsonAdaptedMetric> source) {
        this.metrics.addAll(source);
    }

    public JsonSerializableMetric(MetricList source) {
        List<Metric> convertedList = source.getObservableList();
        metrics.addAll(convertedList.stream().map(JsonAdaptedMetric::new).collect(Collectors.toList()));
    }
    /**
     * Converts into an MetricList
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public MetricList toModelType() throws IllegalValueException {
        MetricList newData = new MetricList();
        for (JsonAdaptedMetric jsonAdaptedmetrics : metrics) {
            Metric metric = jsonAdaptedmetrics.toModelType();
            newData.getObservableList().add(metric);

        }
        return newData;
    }
}
