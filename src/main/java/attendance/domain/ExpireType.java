package attendance.domain;

import java.util.Arrays;

public enum ExpireType {
    EXPIRE("제적", 6),
    FACE_TO_FACE("면담", 3),
    WARNING("경고", 2),
    SAFE("생존", 0);
    private static final long CONVERT_COUNT = 3L;
    private final String display;
    private final long threshold;

    ExpireType(String display, long threshold) {
        this.display = display;
        this.threshold = threshold;
    }


    public static Long convertAbsentCount(long absent, long late) {
        return (late / CONVERT_COUNT) + absent;
    }

    public static ExpireType computeByPenalty(long penalty) {
        return Arrays.stream(values())
                .filter(x -> x.ge(penalty))
                .findFirst()
                .orElse(SAFE);
    }

    public String getDisplay() {
        return display;
    }

    public boolean ge(long penalty) {
        return penalty >= this.threshold;
    }

    public boolean isExpire() {
        return this != SAFE;
    }
}
