package attendance.domain;

import java.time.Duration;
import java.util.Arrays;

public enum Status {
    ABSENT("결석", 30),
    LATE("지각", 5),
    SAFE("출석", 0);
    private final String display;
    private final Duration threshold;

    public String getDisplay() {
        return display;
    }

    public boolean isDelay(Duration delay) {
        return delay.compareTo(this.threshold) > 0;
    }

    Status(String display, int minute) {
        this.display = display;
        this.threshold = Duration.ofMinutes(minute);
    }

    public static Status computeByDelay(Duration delay) {
        return Arrays.stream(values())
                .filter(x -> x.isDelay(delay))
                .findFirst()
                .orElse(SAFE);
    }

}
