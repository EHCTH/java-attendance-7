package attendance.domain;

import java.time.LocalTime;

public enum CampusTime {
    OPEN(8),
    CLOSE(23);
    private final LocalTime time;

    CampusTime(int hour) {
        this.time = LocalTime.of(hour, 0);
    }

    public static boolean isWithin(LocalTime now) {
        return !now.isBefore(OPEN.time) && !now.isAfter(CLOSE.time);
    }
}
