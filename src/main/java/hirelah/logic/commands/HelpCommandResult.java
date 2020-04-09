package hirelah.logic.commands;

import hirelah.ui.MainWindow;

/**
 * A CommandResult that opens the User Guide to help the user.
 */
public class HelpCommandResult extends CommandResult {
    public HelpCommandResult(String feedbackToUser) {
        super(feedbackToUser);
    }

    @Override
    public void displayResult(MainWindow mainWindow) {
        super.displayResult(mainWindow);
        mainWindow.handleHelp();
    }
}
