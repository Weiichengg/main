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
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.QuestionList;
import hirelah.model.hirelah.Transcript;
import hirelah.model.hirelah.exceptions.IllegalActionException;

/**
 * A class that handles saving to and read from the transcript directory.
 */
public class TranscriptStorage {
    private static final Logger logger = LogsCenter.getLogger(TranscriptStorage.class);
    private final Path directory;

    public TranscriptStorage(Path newDirectory) {
        this.directory = newDirectory;
    }

    public Path getPath() {
        return this.directory;
    }

    /**
     * reads the data from the current Path to
     * retrieve all the information regarding Transcript.
     * @return OptionalTranscriptList
     * @throws DataConversionException error when reading the file
     */
    public Optional<Transcript> readTranscript(int id, QuestionList questionList, AttributeList attributeList)
            throws DataConversionException {
        Path filePath = directory.resolve(id + ".json");

        Optional<JsonSerializableTranscript> jsonTranscript = JsonUtil.readJsonFile(
                filePath, JsonSerializableTranscript.class);
        if (jsonTranscript.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTranscript.get().toModelType(questionList, attributeList));
        } catch (IllegalValueException | IllegalActionException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Save the information of the Transcript
     * @param interviewee of the data. Cannot be null.
     */
    public void saveTranscript(Interviewee interviewee) throws IOException {
        requireNonNull(interviewee);
        requireNonNull(directory);
        Path path = directory.resolve(interviewee.getId() + ".json");
        FileUtil.createIfMissing(path);
        Transcript transcript = interviewee.getTranscript().get();
        JsonUtil.saveJsonFile(new JsonSerializableTranscript(transcript), path);
    }

}
