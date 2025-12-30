package attendance.domain.vo;

import java.util.Arrays;

public enum Expire {

    EXPIRE("제적", 6),
    FACE_TO_FACE("면담", 3),
    WARNING("경고", 2),
    SAFE("안전", 0);

    private final String display;
    private final long threshold;

    Expire(String display, long threshold) {
        this.display = display;
        this.threshold = threshold;
    }
    public static Expire findByThreshold(long threshold) {
        return Arrays.stream(values())
                .filter(expire -> expire.threshold <= threshold)
                .findFirst()
                .orElse(SAFE);
    }

    public String getDisplay() {
        return display;
    }

    public boolean isExpire() {
        return !(this == SAFE);
    }
}
