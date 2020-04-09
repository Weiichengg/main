package hirelah.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hirelah.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path sessionsDirectory = Paths.get("data");

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setSessionsDirectory(newUserPrefs.getSessionsDirectory());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getSessionsDirectory() {
        return sessionsDirectory;
    }
    public Path getIntervieweeDirectory() {
        return sessionsDirectory.resolve("interviewee.json");
    }
    public Path getAttributeDirectory() {
        return sessionsDirectory.resolve("attribute.json");
    }
    public Path getQuestionDirectory() {
        return sessionsDirectory.resolve("question.json");
    }
    public Path getMetricDirectory() {
        return sessionsDirectory.resolve("metric.json");
    }
    public Path getTranscriptDirectory() {
        return sessionsDirectory.resolve("transcript");
    }
    public Path getModelDirectory() {
        return sessionsDirectory.resolve("model.json");
    }

    public void setSessionsDirectory(Path sessionsDirectory) {
        requireNonNull(sessionsDirectory);
        this.sessionsDirectory = sessionsDirectory;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings)
                && sessionsDirectory.equals(o.sessionsDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, sessionsDirectory);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data directory location : " + sessionsDirectory);
        return sb.toString();
    }

}
