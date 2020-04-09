package hirelah.model.hirelah;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.exceptions.IllegalActionException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A collection of Interviewees that does not allow 2 Interviewees with the exact same full name.
 * It exposes a single accessor method, getInterviewee, which handles all forms of access by id,
 * alias or full name.
 */
public class IntervieweeList {

    /* The unique ID assigned to an interviewee for the entire session. */
    private int uniqueIntervieweeId;
    /* The actual collection of interviewees. */
    private Map<Integer, Interviewee> interviewees;
    /* Mappings from aliases and full names to the interviewee indices for efficient lookup. */
    private Map<String, Integer> identifierIndices;
    private ObservableList<Interviewee> observableList;

    /**
     * Initializes an IntervieweeList with data from a saved session.
     *
     * @param uniqueIntervieweeId The next id to assign from the previous session.
     * @param interviewees The collection of interviewees from the previous session.
     * @param identifierIndices The identifiers of interviewees from the previous session.
     */
    public IntervieweeList(int uniqueIntervieweeId,
                           Map<Integer, Interviewee> interviewees,
                           Map<String, Integer> identifierIndices) {
        this.uniqueIntervieweeId = uniqueIntervieweeId;
        this.interviewees = interviewees;
        this.identifierIndices = identifierIndices;
        this.observableList = FXCollections.observableArrayList(
            interviewee -> new Observable[] {
                interviewee.fullNameProperty(),
                interviewee.aliasProperty(),
                interviewee.resumeProperty(),
                interviewee.transcriptProperty()
            });
        this.observableList.addAll(interviewees.values());
    }

    /**
     * Initializes a new empty IntervieweeList with no interviewees. uniqueInterviewId starts at 1.
     */
    public IntervieweeList() {
        this(1, new TreeMap<>(), new TreeMap<>());
    }

    /**
     * Restores an IntervieweeList from saved session.
     *
     * @param id the current uniqueIntervieweeId.
     * @param storedInterviewees the list of stored interviewees.
     * @return The restored {@code IntervieweeList}.
     * @throws IllegalValueException if the saved interviewees result in an invalid IntervieweeList.
     */
    public static IntervieweeList fromList(int id, List<Interviewee> storedInterviewees) throws IllegalValueException {
        IntervieweeList intervieweeList = new IntervieweeList();
        intervieweeList.uniqueIntervieweeId = id;
        for (Interviewee interviewee : storedInterviewees) {
            if (interviewee.getId() >= id
                    || intervieweeList.interviewees.containsKey(interviewee.getId())) {
                throw new IllegalValueException("Illegal interviewee id value");
            }
            intervieweeList.interviewees.put(interviewee.getId(), interviewee);

            if (intervieweeList.identifierIndices.containsKey(interviewee.getFullName())
                    || (interviewee.getAlias().isPresent()
                    && intervieweeList.identifierIndices.containsKey(interviewee.getAlias().get()))) {
                throw new IllegalValueException("Duplicate identifiers");
            }
            intervieweeList.identifierIndices.put(interviewee.getFullName(), interviewee.getId());
            interviewee.getAlias().ifPresent(alias ->
                    intervieweeList.identifierIndices.put(alias, interviewee.getId()));
        }
        intervieweeList.observableList.addAll(intervieweeList.interviewees.values());
        return intervieweeList;
    }

    public ObservableList<Interviewee> getObservableList() {
        return FXCollections.unmodifiableObservableList(observableList);
    }

    public int getUniqueIntervieweeId() {
        return this.uniqueIntervieweeId;
    }

    /**
     * Attempts to create a new Interviewee object and add it to the list.
     *
     * @param name The full name of the new Interviewee.
     * @throws IllegalValueException In the following situations:
     *  - The name given is already taken.
     *  - The name given is invalid (is either blank or is a number).
     */
    public void addInterviewee(String name) throws IllegalValueException {
        checkDuplicateIdentifier(name);

        Interviewee interviewee = createInterviewee(name);

        interviewees.put(interviewee.getId(), interviewee);
        identifierIndices.put(name, interviewee.getId());
        observableList.add(interviewee);
    }

    /**
     * A convenience function to add an interviewee and assign an alias immediately. It can fail after
     * adding the interviewee, while assigning the alias, if the alias is already taken. In that case,
     * the interviewee is still added, but the alias is not assigned.
     *
     * @param name The full name of the new Interviewee.
     * @param alias The alias to give.
     * @throws IllegalValueException If the name or alias is already taken or invalid.
     * @throws IllegalActionException Should not be thrown as the interviewee definitely exists, and does
     *                                not have an alias when initially created.
     */
    public void addIntervieweeWithAlias(String name, String alias)
            throws IllegalValueException, IllegalActionException {
        addInterviewee(name);
        setAlias(name, alias);
    }

    /**
     * Sets an alias for the given interviewee which can be used to retrieve said interviewee via getInterviewee.
     *
     * @param identifier The identifier to retrieve the interviewee.
     * @param alias The alias to give.
     * @throws IllegalValueException If the alias is already taken, or is invalid.
     * @throws IllegalActionException In the following situations:
     *  - The identifier cannot be associated with any interviewee.
     */
    public void setAlias(String identifier, String alias) throws IllegalValueException, IllegalActionException {
        checkDuplicateIdentifier(alias);

        Interviewee interviewee = getInterviewee(identifier);

        // Store old alias before overwriting, to delete old alias from identifierIndices after setting
        // in case the new alias is invalid and Interviewee#setAlias throws an Exception.
        Optional<String> oldAlias = interviewee.getAlias();
        interviewee.setAlias(alias);
        oldAlias.ifPresent(old -> identifierIndices.remove(old));
        identifierIndices.put(alias, interviewee.getId());
    }

    /**
     * Sets the full name of the given interviewee.
     *
     * @param identifier The identifier to retrieve the interviewee.
     * @param fullName The new name to overwrite the interviewee's previous full name.
     * @throws IllegalValueException If fullName is already taken, or is invalid.
     * @throws IllegalActionException In the following situations:
     *  - The identifier cannot be associated with any interviewee.
     */
    public void setName(String identifier, String fullName) throws IllegalValueException, IllegalActionException {
        checkDuplicateIdentifier(fullName);

        Interviewee interviewee = getInterviewee(identifier);

        // Store old full name before overwriting, to delete from identifierIndices after setting
        // in case the new full name is invalid and Interviewee#setFullName throws an Exception.
        String oldName = interviewee.getFullName();
        interviewee.setFullName(fullName);
        identifierIndices.remove(oldName);
        identifierIndices.put(fullName, interviewee.getId());
    }

    public void setNameAndAlias(String identifier, String fullName, String alias) throws IllegalValueException,
            IllegalActionException {
        checkDuplicateIdentifier(fullName);
        checkDuplicateIdentifier(alias);

        Interviewee interviewee = getInterviewee(identifier);

        // Store old full name before overwriting, to delete from identifierIndices after setting
        // in case the new full name is invalid and Interviewee#setFullName throws an Exception.
        String oldName = interviewee.getFullName();
        interviewee.setFullName(fullName);
        interviewee.setAlias(alias);
        identifierIndices.remove(oldName);
        identifierIndices.put(fullName, interviewee.getId());
    }

    /**
     * Deletes an Interviewee with the given identifier.
     *
     * @param identifier The identifier to retrieve the interviewee.
     * @throws IllegalActionException If the identifier cannot be associated with any interviewee.
     */
    public void deleteInterviewee(String identifier) throws IllegalActionException {
        Interviewee toDelete = getInterviewee(identifier);
        interviewees.remove(toDelete.getId());
        identifierIndices.remove(toDelete.getFullName());
        observableList.remove(toDelete);
        toDelete.getAlias().ifPresent(alias -> identifierIndices.remove(alias));
    }

    /**
     * Retrieves an Interviewee given a unique identifier, which can be the Interviewee's id, full name or alias.
     *
     * @param identifier The Interviewee's id, full name or an alias.
     * @return The retrieved Interviewee.
     * @throws IllegalActionException If the identifier cannot be associated with any interviewee.
     */
    public Interviewee getInterviewee(String identifier) throws IllegalActionException {
        Interviewee result;
        try {
            // attempts to parse identifier as an interviewee id
            int id = Integer.parseUnsignedInt(identifier);
            result = interviewees.get(id);
        } catch (NumberFormatException e) {
            // if not an id, attempts to match identifier with either full name or alias
            Integer id = identifierIndices.get(identifier);
            if (id == null) {
                throw new IllegalActionException("No interviewee with the given identifier can be found");
            }
            result = interviewees.get(id);
        }

        if (result == null) {
            throw new IllegalActionException("No interviewee with this id can be found");
        }
        return result;
    }

    /**
     * Creates an Interviewee with the given name, assigning it the next uniqueIntervieweeId.
     * Only increments the id if the interviewee was created successfully.
     *
     * @param name The name of the interviewee.
     * @return A new Interviewee object.
     * @throws IllegalValueException If the name given is an invalid name.
     */
    private Interviewee createInterviewee(String name) throws IllegalValueException {
        Interviewee interviewee = new Interviewee(name, uniqueIntervieweeId);

        // Only increment id if interviewee was successfully created
        uniqueIntervieweeId++;
        return interviewee;
    }

    private void checkDuplicateIdentifier(String identifier) throws IllegalValueException {
        if (identifierIndices.containsKey(identifier)) {
            throw new IllegalValueException("An Interviewee with this name or alias already exists!");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IntervieweeList // instanceof handles nulls
                && interviewees.equals(((IntervieweeList) other).interviewees)); // state check
    }
    @Override
    public int hashCode() {
        return observableList.hashCode();
    }
}
