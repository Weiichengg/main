
package hirelah.logic.commands;

import hirelah.ui.MainWindow;

/**
 * Constructs a {@code NavigationCommandResult} with the specified {@code feedbackToUser},
 * and index as specified to enable ToggleView Transcript to navigate to the particular index.
 */
public class NavigationCommandResult extends CommandResult {
    /**
     * The index of a remark for ToggleView Transcript
     **/
    private int index;

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public NavigationCommandResult(String feedbackToUser, int index) {
        super(feedbackToUser);
        this.index = index;
    }

    @Override
    public void displayResult(MainWindow mainWindow) {
        super.displayResult(mainWindow);
        mainWindow.scrollTranscriptTo(index);
    }

    /**
     * Retrieves the index of this {@code NavigationCommandResult}
     * which is the index of the remark to be navigated to.
     *
     * @return index of the {@code NavigationCommandResult}
     */
    public int getIndex() {
        return this.index;
    }
}
