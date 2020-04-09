package hirelah.model.hirelah;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class RemarkTest {
    public static final Duration DEFAULT_START_TIME = Duration.ofSeconds(0);
    public static final Duration DEFAULT_EARLIER_TIME = DEFAULT_START_TIME.plusSeconds(43);
    public static final Duration DEFAULT_MIDDLE_TIME = DEFAULT_START_TIME.plusSeconds(100);
    public static final Duration DEFAULT_LATER_TIME = DEFAULT_START_TIME.plusSeconds(1003);
    public static final Remark REMARK_START = new Remark(DEFAULT_START_TIME, "Start");
    public static final Remark REMARK_EARLIER = new Remark(DEFAULT_EARLIER_TIME, "Earlier");
    public static final Remark REMARK_MIDDLE = new Remark(DEFAULT_MIDDLE_TIME, "Random remark");
    public static final Remark REMARK_LATER = new Remark(DEFAULT_LATER_TIME, "Stop");

    @Test
    void getTime_maxTime_success() {
        assertEquals(DEFAULT_MIDDLE_TIME, REMARK_MIDDLE.getTime());
    }

    @Test
    void getTimeString_formattedProperly() {
        assertEquals("0:00", REMARK_START.getTimeString());
        assertEquals("0:43", REMARK_EARLIER.getTimeString());
        assertEquals("1:40", REMARK_MIDDLE.getTimeString());
        assertEquals("16:43", REMARK_LATER.getTimeString());
    }

    @Test
    void equals_sameMessageAndTime_true() {
        Remark remark1 = new Remark(DEFAULT_EARLIER_TIME, "Remark");
        Remark remark2 = new Remark(DEFAULT_EARLIER_TIME, "Remark");
        assertEquals(remark1, remark2);
    }

}
