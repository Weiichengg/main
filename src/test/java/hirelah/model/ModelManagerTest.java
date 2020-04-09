package hirelah.model;

import static hirelah.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import hirelah.commons.core.GuiSettings;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        //assertEquals(new IntervieweeList(), modelManager.getIntervieweeList()); TODO: override equals on model classes
        //assertEquals(new AttributeList(), modelManager.getAttributeList());
        //assertEquals(new QuestionList(), modelManager.getQuestionList());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setSessionsDirectory(Paths.get("hirelah/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setSessionsDirectory(Paths.get("new/hirelah/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setSessionsDirectory_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setSessionsDirectory(null));
    }

    @Test
    public void setSessionsDirectory_validPath_setsSessionsDirectory() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setSessionsDirectory(path);
        assertEquals(path, modelManager.getSessionsDirectory());
    }

}
