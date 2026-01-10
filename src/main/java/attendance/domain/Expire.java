package attendance.domain;

import java.security.PublicKey;
import java.util.Arrays;

public enum Expire {

    EXPIRE("제적", 6),
    FACE_TO_FACE("면담",3),
    WARNING("경고",2),
    SAFE("생존", 0);
    private final String display;
    private final long threshold;

    Expire(String display, long threshold) {
        this.display = display;
        this.threshold = threshold;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isExpire() {
        return this != SAFE;
    }

    public static Expire findByAbsentCount(long count) {
        return Arrays.stream(values())
                .filter(x -> x.threshold <= count)
                .findFirst()
                .orElse(SAFE);
    }
}
