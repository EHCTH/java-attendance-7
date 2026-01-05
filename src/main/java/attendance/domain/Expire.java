package attendance.domain;

import java.util.Arrays;

public enum Expire {
    EXPIRE("제적", 6),
    FACE_TO_FACE("면담", 3),
    WARNING("경고", 2),
    SAFE("생존", 0);
    private final String display;
    private final long threshold;

    Expire(String display, long threshold) {
        this.display = display;
        this.threshold = threshold;
    }
    public static long toAbsentCount(long absentCount, long lateCount) {
        return (lateCount / 3) + absentCount;
    }

    public static Expire computeByThreshold(long threshold) {
        return Arrays.stream(values())
                .filter(expire -> expire.ge(threshold))
                .findFirst()
                .orElse(SAFE);
    }

    public String getDisplay() {
        return display;
    }

    public boolean ge(long threshold) {
        return threshold >= this.threshold;
    }


    public boolean isExpire() {
        return !isSafe();
    }

    public boolean isSafe() {
        return this == SAFE;
    }
}
