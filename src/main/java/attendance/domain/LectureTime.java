package attendance.domain;

import java.security.PublicKey;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public enum LectureTime {
    월(DayOfWeek.MONDAY,13, 18),
    화(DayOfWeek.TUESDAY, 10, 18),
    수(DayOfWeek.WEDNESDAY,10, 18),
    목(DayOfWeek.THURSDAY,10, 18),
    금(DayOfWeek.FRIDAY,10, 18);
    private final DayOfWeek dayOfWeek;
    private final LocalTime open;
    private final LocalTime close;
    private static final DateTimeFormatter OUTPUT =
            DateTimeFormatter.ofPattern("MM월 dd일 EEEE");

    LectureTime(DayOfWeek dayOfWeek, int openHour, int closeHour) {
        this.dayOfWeek = dayOfWeek;
        this.open = LocalTime.of(openHour, 0);
        this.close = LocalTime.of(closeHour, 0);
    }

    public static LectureTime findByDate(LocalDate localDate) {
        return findByDayOfWeek(localDate.getDayOfWeek())
                .orElseThrow(() -> new IllegalArgumentException(messageConverter(localDate)));
    }
    public static Optional<LectureTime> findByDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(x -> x.dayOfWeek.equals(dayOfWeek))
                .findFirst();
    }
    public Duration computeDelayByTime(LocalTime localTime) {
        if (localTime.isBefore(open)) {
            return Duration.ZERO;
        }
        return Duration.between(open, localTime);
    }
    private static String messageConverter(LocalDate localDate) {
        return "[ERROR] " + localDate.format(OUTPUT) + "은 등교일이 아닙니다.";
    }
}
