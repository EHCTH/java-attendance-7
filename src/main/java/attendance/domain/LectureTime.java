package attendance.domain;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public enum LectureTime {

    MONDAY(DayOfWeek.MONDAY, 13, 18),
    TUESDAY(DayOfWeek.TUESDAY, 10, 18),
    WEDNESDAY(DayOfWeek.WEDNESDAY, 10, 18),
    THURSDAY(DayOfWeek.THURSDAY, 10, 18),
    FRIDAY(DayOfWeek.FRIDAY, 10, 18),
    SATURDAY(DayOfWeek.SATURDAY, 10, 18),
    SUNDAY(DayOfWeek.SUNDAY, 10, 18);

    private final DayOfWeek dayOfWeek;
    private final LocalTime open;
    private final LocalTime close;


    LectureTime(DayOfWeek dayOfWeek, int open, int close) {
        this.dayOfWeek = dayOfWeek;
        this.open = LocalTime.of(open, 0);
        this.close = LocalTime.of(close, 0);
    }
    public static LectureTime findByDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(x -> x.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 요일입니다"));
    }

    public Duration computeDuration(LocalTime localTime) {
        if (localTime.isBefore(open)) {
            return Duration.ZERO;
        }
        return Duration.between(open, localTime);
    }
}
