package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.hirelah.storage.Storage;

/**
 * FinaliseCommand finalises the properties of an interview session
 * which includes the attributes, the questions and interviewees.
 */

public class FinaliseCommand extends Command {

    public static final String COMMAND_WORD = "finalise";

    public static final String MESSAGE_SUCCESS = "Attributes and questions of this interview session"
            + " has been finalised. You cannot change them anymore.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finalises the attributes and questions "
            + "of an interview session.";


    @Override
    public CommandResult execute(Model model, Storage storage) {
        requireNonNull(model);
        model.finaliseInterviewProperties();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FinaliseCommand); // instanceof handles nulls
    }
}
