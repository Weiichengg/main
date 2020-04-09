package hirelah.commons.util;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;

/**
 * A container for Model specific utility functions
 */
public class ModelUtil {
    private static final String MODEL_HAS_BEEN_FINALISED = "This interview session has been finalized. You can no "
            + "longer make changes to attributes and questions.";
    private static final String MODEL_HAS_NOT_BEEN_FINALISED = "This interview session has not been finalized. You "
            + "can't create metrics, conduct interviews, or find best interviewees.";

    /**
     * Validates whether the Model isFinalized state, matches to
     * what the command desires.
     * @param model The corresponding model.
     * @param isFinalised The boolean flag to determine whether a command wants to be executed in finalized mode or not.
     * @throws CommandException If the desired flag does not match with the current model state.
     */
    public static void validateFinalisation(Model model, boolean isFinalised) throws CommandException {
        if (!isFinalised && model.isFinalisedInterviewProperties()) {
            throw new CommandException(MODEL_HAS_BEEN_FINALISED);
        } else if (isFinalised && !model.isFinalisedInterviewProperties()) {
            throw new CommandException(MODEL_HAS_NOT_BEEN_FINALISED);
        }
    }
}
