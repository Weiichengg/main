package hirelah.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import hirelah.commons.core.LogsCenter;
import hirelah.commons.exceptions.DataConversionException;
import hirelah.commons.exceptions.IllegalValueException;
import hirelah.commons.util.FileUtil;
import hirelah.commons.util.JsonUtil;
import hirelah.model.hirelah.MetricList;

/**
 * MetricStorage containing the file path
 * to store the metric objects.
 */
public class MetricStorage {
    private static final Logger logger = LogsCenter.getLogger(MetricStorage.class);
    private final Path path;

    public MetricStorage(Path newPath) {
        this.path = newPath;
    }

    public Path getPath() {
        return this.path;
    }

    /**
     * reads the data from the current Path to
     * retrieve all the information regarding Metric.
     * @return OptionalMetricList
     * @throws DataConversionException error in reading the file.
     */
    public Optional<MetricList> readMetric(Path filePath) throws DataConversionException {
        requireNonNull(filePath);
        Optional<JsonSerializableMetric> jsonMetric = JsonUtil.readJsonFile(
                filePath, JsonSerializableMetric.class);
        if (jsonMetric.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonMetric.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Save the information of the Metric
     * @param  source of the data. Cannot be null.
     */
    public void saveMetrics(MetricList source) throws IOException {
        requireNonNull(source);
        requireNonNull(path);
        FileUtil.createIfMissing(path);
        JsonUtil.saveJsonFile(new JsonSerializableMetric(source), path);
    }
}
