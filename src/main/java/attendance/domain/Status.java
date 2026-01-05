package attendance.domain;

import java.time.Duration;
import java.util.Arrays;

public enum Status {
    ABSENT("결석", 30L),
    LATE("지각", 5L),
    PRESENT("출석", 0L);
    private final String display;
    private final Duration threshold;

    Status(String display, Long threshold) {
        this.display = display;
        this.threshold = Duration.ofMinutes(threshold);
    }

    public static Status computeByDelay(Duration delay) {
        return Arrays.stream(values())
                .filter(x -> x.isDelay(delay))
                .findFirst()
                .orElse(PRESENT);
    }

    public String getDisplay() {
        return display;
    }

    public boolean isDelay(Duration delay) {
        return delay.compareTo(this.threshold) > 0;
    }


}
