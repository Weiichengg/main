package hirelah.testutil;

import static hirelah.testutil.TypicalTranscript.getTypicalTranscript;

import java.util.List;

import hirelah.commons.exceptions.IllegalValueException;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;

/** A utility class containing a list of {@code Intervieweelist}  to be used in tests.*/
public class TypicalInterviewee {
    public static final String NAME1 = "Tom";
    public static final String NAME2 = "JANE";
    public static final int ID1 = 1;
    public static final int ID2 = 2;
    public static final String ALIAS1 = "lucky boy";
    public static final String ALIAS2 = "lucky girl";
    /** method to create sample interviewee*/
    public static Interviewee buildInterviewee(String name, int id, String alias) throws IllegalValueException {
        Interviewee typicalInterviewee = new Interviewee(name, id);
        typicalInterviewee.setAlias(alias);
        typicalInterviewee.setTranscript(getTypicalTranscript());
        return typicalInterviewee;
    }
    public static IntervieweeList getIntervieweeList() throws IllegalValueException {
        List<Interviewee> list;
        list = List.of(buildInterviewee(NAME1, ID1, ALIAS1),
                buildInterviewee(NAME2, ID2, ALIAS2));
        return IntervieweeList.fromList(3, list);
    }
}
