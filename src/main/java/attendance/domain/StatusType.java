package attendance.domain;

import java.time.Duration;
import java.util.Arrays;

public enum StatusType {
    ABSENT("결석", 30),
    LATE("지각", 5),
    SAFE("출석", 0);
    private final String display;
    private final Duration duration;

    StatusType(String display, long minute) {
        this.display = display;
        this.duration = Duration.ofMinutes(minute);
    }
    public static StatusType computeByDurationDelay(Duration delay) {
        return Arrays.stream(values())
                .filter(x -> x.isDelay(delay))
                .findFirst()
                .orElse(SAFE);
    }

    public String getDisplay() {
        return display;
    }

    public boolean isDelay(Duration delay) {
        return delay.compareTo(this.duration) > 0;
    }
}
