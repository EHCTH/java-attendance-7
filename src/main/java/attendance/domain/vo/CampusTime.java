package attendance.domain.vo;

import java.time.LocalTime;

public enum CampusTime {
    OPEN(8,0),
    CLOSE(23, 0);

    private final LocalTime time;

    CampusTime(int hour, int minute) {
        this.time = LocalTime.of(hour, minute, 0);
    }

    private static boolean isCampusOpen(LocalTime time) {
        return !time.isBefore(OPEN.time) && !time.isAfter(CLOSE.time);
//        return !(time.isBefore(OPEN.time) || time.isAfter(CLOSE.time));
    }
    public static void validate(LocalTime time) {
        if (!isCampusOpen(time)) {
            throw new IllegalArgumentException("[ERROR] 캠퍼스 운영시간이 아닙니다");
        }
    }


}
