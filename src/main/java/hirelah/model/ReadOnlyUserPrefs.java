package hirelah.model;

import java.nio.file.Path;

import hirelah.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getSessionsDirectory();

}
