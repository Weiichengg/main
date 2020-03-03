package seedu.address.model.hirelah;

import java.io.File;
import java.util.Map;

/**
 * A simple record class that contains all interview information for a Interviewee - the scores, the transcript
 * and the audio recording.
 */
public class InterviewSession {
    private Transcript transcript;
    private Map<Attribute, Integer> scores;
    private File audioRecording;

    public InterviewSession(Transcript transcript, Map<Attribute, Integer> scores, File audioRecording) {
        this.transcript = transcript;
        this.scores = scores;
        this.audioRecording = audioRecording;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public Map<Attribute, Integer> getScores() {
        return scores;
    }

    public File getAudioRecording() {
        return audioRecording;
    }
}
