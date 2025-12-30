package attendance.domain.vo;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public enum KorDayOfWeek {
    MONDAY(DayOfWeek.MONDAY,"월요일", LectureTime.monday()),
    TUESDAY(DayOfWeek.TUESDAY,"화요일", LectureTime.other()),
    WEDNESDAY(DayOfWeek.WEDNESDAY, "수요일", LectureTime.other()),
    THURSDAY(DayOfWeek.THURSDAY,"목요일", LectureTime.other()),
    FRIDAY(DayOfWeek.FRIDAY,"금요일", LectureTime.other()),
    SATURDAY(DayOfWeek.SATURDAY,"토요일", LectureTime.other()),
    SUNDAY(DayOfWeek.SUNDAY,"일요일", LectureTime.other());
    private final DayOfWeek dayOfWeek;
    private final String display;
    private final LectureTime lectureTime;

    KorDayOfWeek(DayOfWeek dayOfWeek, String display, LectureTime lectureTime) {
        this.dayOfWeek = dayOfWeek;
        this.display = display;
        this.lectureTime = lectureTime;
    }

    public static KorDayOfWeek findByDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(korDayOfWeek -> korDayOfWeek.dayOfWeek.equals(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 그런 날짜는 존재하지 않습니다"));
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDisplay() {
        return display;
    }

    public LectureTime getLectureTime() {
        return lectureTime;
    }

    public boolean isWeekDay() {
        return !(this == SATURDAY || this == SUNDAY);
    }
    public Status computeStatus(LocalTime localTime) {
        if (localTime == null) {
            return Status.ABSENT;
        }
        CampusTime.validate(localTime);
        validate();
        Duration delay = lectureTime.between(localTime);
        return Status.computeTime(delay);
    }
    private void validate() {
        if (isWeekDay()) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 주말에는 강의를 하지 않습니다");
    }
}
