package attendance.domain;

import java.time.LocalTime;

public enum CampusTime {
    OPEN(8),CLOSE(23);
    private final LocalTime localTime;

    CampusTime(int hour) {
        this.localTime = LocalTime.of(hour, 0);
    }

    public static boolean isWithin(LocalTime now) {
        return !now.isBefore(OPEN.localTime) && now.isBefore(CLOSE.localTime);
    }


}
