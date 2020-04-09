package hirelah.logic.commands;

import hirelah.ui.MainWindow;

/**
 * A CommandResult that changes the main view of the app.
 */
public class ToggleCommandResult extends CommandResult {
    private final ToggleView toggleView;

    /**
     * Constructs a {@code ToggleCommandResult} with the specified {@code feedbackToUser},
     * that will toggle the UI to the given ToggleView.
     */
    public ToggleCommandResult(String feedbackToUser, ToggleView toggleView) {
        super(feedbackToUser);
        this.toggleView = toggleView;
    }

    @Override
    public void displayResult(MainWindow mainWindow) {
        super.displayResult(mainWindow);
        mainWindow.handleToggle(toggleView);
    }
}
