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
import hirelah.model.hirelah.QuestionList;

/**
 * QuestionStorage containing the file path
 * to store the question objects.
 */
public class QuestionStorage {

    private static final Logger logger = LogsCenter.getLogger(QuestionStorage.class);
    private final Path path;


    public QuestionStorage(Path newPath) {
        this.path = newPath;
    }

    public Path getPath() {
        return this.path;
    }

    public Optional<QuestionList> readQuestion() throws DataConversionException {
        return readQuestion(this.path);
    }

    /**
     * reads the data from the current Path to
     * retrieve all the information regarding Question.
     * @return OptionalQuestionList
     * @throws DataConversionException error in reading the file
     */
    public Optional<QuestionList> readQuestion(Path filePath) throws DataConversionException {
        requireNonNull(filePath);
        Optional<JsonSerializableQuestion> jsonQuestion = JsonUtil.readJsonFile(
                filePath, JsonSerializableQuestion.class);
        if (jsonQuestion.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonQuestion.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Save the information of the Question
     * @param  source of the data. Cannot be null.
     */
    public void saveQuestions(QuestionList source) throws IOException {
        requireNonNull(source);
        requireNonNull(path);
        FileUtil.createIfMissing(path);
        JsonUtil.saveJsonFile(new JsonSerializableQuestion(source), path);
    }
}
