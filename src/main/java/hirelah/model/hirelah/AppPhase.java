package hirelah.model.hirelah;

/**
 * An enum indicating the mode the app is operating in currently, affecting the Parser used and thus the commands
 * understood by the app
 * <ul>
 *     <li>Pre session - Before opening a session. Can only open/create/delete sessions</li>
 *     <li>Normal - After a session is opened. Can perform most CRUD operations with the Model</li>
 *     <li>Interview - During an interview. Can record remarks, score attributes and answer questions</li>
 * </ul>
 */
public enum AppPhase {
    PRE_SESSION,
    NORMAL,
    INTERVIEW
}
