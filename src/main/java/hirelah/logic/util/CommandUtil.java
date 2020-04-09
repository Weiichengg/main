package hirelah.logic.util;

import java.io.IOException;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;
/**
 * Help to save the state of the system upon executing a particular command.
 */
public class CommandUtil {
    public static final String SAVE_ATTRIBUTE_ERROR_MESSAGE = "Could not save the updated attributes to file: ";
    public static final String SAVE_INTERVIEWEE_ERROR_MESSAGE = "Could not save the updated interviewees to file: ";
    public static final String SAVE_METRICS_ERROR_MESSAGE = "Could not save the updated metrics to file: ";
    public static final String SAVE_QUESTIONS_ERROR_MESSAGE = "Could not save the updated questions to file: ";
    public static final String SAVE_MODEL_ERROR_MESSAGE = "Could not save the updated model status to file: ";
    /**
     * Saves the updated AttributeList to the storage
     */
    public static void saveAttributes(Model model, Storage storage) throws CommandException {
        try {
            storage.saveAttribute(model.getAttributeList());
        } catch (IOException ioe) {
            throw new CommandException(SAVE_ATTRIBUTE_ERROR_MESSAGE + ioe, ioe);
        }
    }

    /**
     * Saves the updated IntervieweeList to the storage
     */
    public static void saveInterviewees(Model model, Storage storage) throws CommandException {
        try {
            storage.saveInterviewee(model.getIntervieweeList());
        } catch (IOException ioe) {
            throw new CommandException(SAVE_INTERVIEWEE_ERROR_MESSAGE + ioe, ioe);
        }
    }

    /**
     * Saves the updated QuestionList to the storage
     */
    public static void saveQuestions(Model model, Storage storage) throws CommandException {
        try {
            storage.saveQuestion(model.getQuestionList());
        } catch (IOException ioe) {
            throw new CommandException(SAVE_QUESTIONS_ERROR_MESSAGE + ioe, ioe);
        }
    }

    /**
     * Saves the updated MetricList to the storage
     */
    public static void saveMetrics(Model model, Storage storage) throws CommandException {
        try {
            storage.saveMetric(model.getMetricList());
        } catch (IOException ioe) {
            throw new CommandException(SAVE_METRICS_ERROR_MESSAGE + ioe, ioe);
        }
    }
    /**Save the finalised state of the model in the storage*/
    public static void saveModel(Model model, Storage storage) throws CommandException {
        try {
            storage.saveModel(model.isFinalisedInterviewProperties());
        } catch (IOException ioe) {
            throw new CommandException(SAVE_MODEL_ERROR_MESSAGE + ioe, ioe);
        }
    }
}
