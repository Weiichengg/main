package hirelah.logic.commands;

import hirelah.ui.MainWindow;

/**
 * A CommandResult that causes the app to exit.
 */
public class ExitCommandResult extends CommandResult {

    public ExitCommandResult(String feedbackToUser) {
        super(feedbackToUser);
    }

    @Override
    public void displayResult(MainWindow mainWindow) {
        super.displayResult(mainWindow);
        mainWindow.handleExit();
    }
}
