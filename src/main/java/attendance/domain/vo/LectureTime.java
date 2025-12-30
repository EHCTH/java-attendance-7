package attendance.domain.vo;

import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalTime;

public class LectureTime {
    private final LocalTime start;
    private final LocalTime end;

    private LectureTime(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }
    public static LectureTime monday() {
        LocalTime start = LocalTime.of(13, 0, 0);
        LocalTime end = LocalTime.of(18, 0, 0);
        return new LectureTime(start, end);
    }
    public static LectureTime other() {
        LocalTime localTime = LocalTime.of(10, 0, 0);
        LocalTime end = LocalTime.of(18, 0, 0);
        return new LectureTime(localTime, end);
    }

    /*
    교육 시간은 월요일 13:00-18:00, 화요일-금요일은 10:00~18:00
     */

    public boolean isWithin(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
//        return !(time.isBefore(start) || time.isAfter(end)); // 드모르간법칙
    }
    public Duration between(LocalTime localTime) {
        if (localTime.isBefore(start)) {
            return Duration.ZERO;
        }
        return Duration.between(start, localTime);
    }
}
