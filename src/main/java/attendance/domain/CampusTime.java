package attendance.domain;

import java.time.LocalTime;

public enum CampusTime {
    OPEN(8),
    CLOSE(23);
    private final LocalTime localTime;

    CampusTime(int time) {
        this.localTime = LocalTime.of(time, 0);
    }

    public static boolean isWithin(LocalTime localTime) {
        return !localTime.isBefore(OPEN.localTime) && !localTime.isAfter(CLOSE.localTime);
    }
}
