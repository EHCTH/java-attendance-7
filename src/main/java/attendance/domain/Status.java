package attendance.domain;

import java.time.Duration;
import java.util.Arrays;

public enum Status {
    ABSENT("결석", Duration.ofMinutes(30L)),
    LATE("지각",Duration.ofMinutes(5L)),
    PRESENT("출석",Duration.ofMinutes(0L));
    private final String display;
    private final Duration duration;

    Status(String display, Duration duration) {
        this.display = display;
        this.duration = duration;
    }

    private boolean isLate(Duration duration) {
        return duration.compareTo(this.duration) > 0;
    }

    public static Status computeByDelay(Duration duration) {
        return Arrays.stream(values())
                .filter(status -> status.isLate(duration))
                .findFirst()
                .orElse(PRESENT);
    }
    public String getDisplay() {
        return display;
    }
}
