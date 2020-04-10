package hirelah.logic.commands;

import static hirelah.logic.util.CommandUtil.saveModel;
import static java.util.Objects.requireNonNull;

import hirelah.logic.commands.exceptions.CommandException;
import hirelah.model.Model;
import hirelah.storage.Storage;

/**
 * FinaliseCommand finalises the properties of an interview session
 * which includes the attributes, the questions and interviewees.
 */

public class FinaliseCommand extends Command {

    public static final String COMMAND_WORD = "finalise";

    public static final String MESSAGE_SUCCESS = "Attributes and questions of this interview session"
            + " has been finalised. You cannot change them anymore.";

    public static final String MESSAGE_FUNCTION = ": Finalises the attributes and questions ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + MESSAGE_FUNCTION
            + "of an interview session.";


    @Override
    public CommandResult execute(Model model, Storage storage) throws CommandException {
        requireNonNull(model);
        model.finaliseInterviewProperties();
        saveModel(model, storage);
        return new ToggleCommandResult(MESSAGE_SUCCESS, ToggleView.INTERVIEWEE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FinaliseCommand); // instanceof handles nulls
    }
}
