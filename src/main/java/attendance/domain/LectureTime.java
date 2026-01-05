package attendance.domain;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public enum LectureTime {
    월(DayOfWeek.MONDAY, 13, 8),
    화(DayOfWeek.TUESDAY, 10, 18),
    수(DayOfWeek.WEDNESDAY, 10, 18),
    목(DayOfWeek.THURSDAY, 10, 18),
    금(DayOfWeek.FRIDAY, 10, 18);
    private static final DateTimeFormatter MESSAGE_FORMATTER =
            DateTimeFormatter.ofPattern("MM월 dd일 EEEE", Locale.KOREA);
    private final DayOfWeek dayOfWeek;
    private final LocalTime open;
    private final LocalTime close;

    LectureTime(DayOfWeek dayOfWeek, Integer open, Integer close) {
        this.dayOfWeek = dayOfWeek;
        this.open = LocalTime.of(open, 0);
        this.close = LocalTime.of(close, 0);
    }

    public static LectureTime findByDayOfWeek(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return Arrays.stream(values())
                .filter(x -> x.dayOfWeek.equals(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(messageConvert(localDate)));
    }

    private static String messageConvert(LocalDate localDate) {
        return "[ERROR] " + localDate.format(MESSAGE_FORMATTER) + "은 등교일이 아닙니다.";
    }

    public Duration computeByDelay(LocalTime localTime) {
        if (isEarlyPresent(localTime)) {
            return Duration.ZERO;
        }
        return Duration.between(open, localTime);
    }
    private boolean isEarlyPresent(LocalTime localTime) {
        return localTime.isBefore(open);
    }

    public boolean isWeekDay() {
        return !isWeekend();
    }

    public boolean isWeekend() {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
