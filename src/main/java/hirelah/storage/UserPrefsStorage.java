package hirelah.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import hirelah.commons.exceptions.DataConversionException;
import hirelah.model.ReadOnlyUserPrefs;
import hirelah.model.UserPrefs;

/**
 * Represents a storage for {@link UserPrefs}.
 */
public interface UserPrefsStorage {

    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getUserPrefsFilePath();

    /**
     * Returns UserPrefs data from storage.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     */
    Optional<UserPrefs> readUserPrefs() throws DataConversionException;

    /**
     * Saves the given {@link ReadOnlyUserPrefs} to the storage.
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

}
