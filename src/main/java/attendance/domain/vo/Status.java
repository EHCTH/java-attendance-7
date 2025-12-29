package attendance.domain.vo;


import java.time.Duration;
import java.util.Arrays;

public enum Status {
    ABSENT("결석", Duration.ofMinutes(30L)),
    LATE("지각", Duration.ofMinutes(5L)),
    CHECK_IN("출석", Duration.ZERO);
    private final String display;
    private final Duration threshold;

    Status(String display, Duration threshold) {
        this.display = display;
        this.threshold = threshold;
    }
    public static Status computeTime(Duration delay) {
        return Arrays.stream(new Status[] {ABSENT, LATE})
                .filter(status -> status.isExceededBy(delay))
                .findFirst()
                .orElse(CHECK_IN);
    }

    public String getDisplay() {
        return display;
    }

    private boolean isExceededBy(Duration delay) {
        return delay.compareTo(this.threshold) > 0;
    }
}
