package attendance.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Period implements Comparable<Period> {
    private final LocalDate localDate;
    private final LocalTime localTime;

    private Period(LocalDate localDate, LocalTime localTime) {
        this.localDate = localDate;
        this.localTime = localTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public static Period present(LocalDateTime localDateTime) {
        return new Period(localDateTime.toLocalDate(), localDateTime.toLocalTime());
    }

    public static Period absent(LocalDate localDate) {
        return new Period(localDate, null);
    }

    public static Period checkIn(LocalDate localDate, LocalTime localTime) {
        return new Period(localDate, localTime);
    }

    public Period withTime(LocalTime localTime) {
        return new Period(this.localDate, localTime);
    }

    public boolean isSameDate(LocalDate localDate) {
        return this.localDate.equals(localDate);
    }

    public Status computeStatus() {
        if (localTime == null) {
            return Status.ABSENT;
        }
        LectureTime lectureTime = LectureTime.findByDayOfWeek(localDate);
        Duration delay = lectureTime.computeByDelay(localTime);
        return Status.computeByDelay(delay);
    }

    @Override
    public int compareTo(Period o) {
        return this.localDate.compareTo(o.localDate);
    }
}
