
package seedu.address.logic.commands;

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
    public NavigationCommandResult(String feedbackToUser, ToggleView toggleView, int index) {
        super(feedbackToUser, false, false, toggleView);
        this.index = index;
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
