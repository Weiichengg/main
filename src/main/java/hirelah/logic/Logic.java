package hirelah.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import hirelah.commons.core.GuiSettings;
import hirelah.logic.commands.CommandResult;
import hirelah.logic.commands.exceptions.CommandException;
import hirelah.logic.parser.exceptions.ParseException;
import hirelah.model.hirelah.Attribute;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeToScore;
import hirelah.model.hirelah.Metric;
import hirelah.model.hirelah.Question;
import javafx.collections.ObservableList;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the list of attributes */
    ObservableList<Attribute> getAttributeListView();

    /** Returns an unmodifiable view of the list of interviewees */
    ObservableList<Interviewee> getIntervieweeListView();

    /** Returns an unmodifiable view of the list of questions */
    ObservableList<Question> getQuestionListView();

    /** Returns an unmodifiable view of the list of metrics */
    ObservableList<Metric> getMetricListView();

    /** Returns the Interviewee currently being looked at*/
    Interviewee getCurrentInterviewee();

    /**
     * Sets the currentInterviewee.
     */
    void setCurrentInterviewee(Interviewee interviewee);

    ObservableList<IntervieweeToScore> getBestNIntervieweesView();

    /**
     * Returns the user prefs' sessions directory.
     */
    Path getSessionsDirectory();

    /** Returns the Path where the current Interview session is stored, if it exists. */
    Optional<Path> getCurrentSession();

    /**
     * Returns if the interview session has been finalised.
     *
     * @see hirelah.logic.commands.FinaliseCommand
     */
    boolean isFinalisedInterviewProperties();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Returns all the available sessions in the user prefs' sessions directory.
     */
    List<File> getAvailableSessions() throws IOException;

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
