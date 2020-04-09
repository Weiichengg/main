package hirelah.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import hirelah.commons.exceptions.DataConversionException;
import hirelah.model.Model;
import hirelah.model.ReadOnlyUserPrefs;
import hirelah.model.hirelah.AttributeList;
import hirelah.model.hirelah.Interviewee;
import hirelah.model.hirelah.IntervieweeList;
import hirelah.model.hirelah.MetricList;
import hirelah.model.hirelah.QuestionList;

/**
 * API of the Storage component.
 */
public interface Storage extends UserPrefsStorage {
    List<File> readSessions(ReadOnlyUserPrefs userPrefs) throws IOException;

    public void loadSession(Model model, Path session) throws DataConversionException;

    void saveInterviewee(IntervieweeList source) throws IOException;

    Optional<IntervieweeList> readInterviewee(QuestionList questionList,
                                              AttributeList attributeList,
                                              Boolean initialModel) throws DataConversionException;

    void saveAttribute(AttributeList source) throws IOException;

    Optional<AttributeList> readAttribute() throws DataConversionException;

    void saveQuestion(QuestionList source) throws IOException;

    Optional<QuestionList> readQuestion() throws DataConversionException;

    void saveMetric(MetricList source) throws IOException;

    Optional<MetricList> readMetric() throws DataConversionException;

    void saveTranscript(Interviewee source) throws IOException;

    void saveModel(Boolean model) throws IOException;

    Optional<Boolean> readModel() throws DataConversionException;
}
