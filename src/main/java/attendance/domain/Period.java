package attendance.domain;

import java.time.*;
import java.util.Objects;

public class Period implements Comparable<Period> {
    private final LocalDate localDate;
    private final LocalTime localTime;

    private Period(LocalDate localDate, LocalTime localTime) {
        this.localDate = localDate;
        this.localTime = localTime;

    }
    public static Period absent(LocalDate localDate) {
        return new Period(localDate, null);
    }

    public static Period present(LocalDate localDate, LocalTime localTime) {
        return new Period(localDate, localTime);
    }

    public static Period present(LocalDateTime localDateTime) {
        return new Period(localDateTime.toLocalDate(), localDateTime.toLocalTime());
    }

    public boolean isDay(int day) {
        return localDate.getDayOfMonth() == day;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public Period withTime(LocalTime localTime) {
        return new Period(this.localDate, localTime);
    }

    public boolean equalsToDate(LocalDate localDate) {
        return this.localDate.equals(localDate);
    }

    public Status computeStatus() {
        if (localTime == null || !CampusTime.isWithin(localTime)) {
            return Status.ABSENT;
        }
        LectureTime lectureTime = LectureTime.findByDayOfWeek(localDate.getDayOfWeek());
        Duration delay = lectureTime.computeDuration(localTime);
        return Status.computeByDelay(delay);
    }
    public boolean isWeekDay() {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Period period)) return false;
        return Objects.equals(localDate, period.localDate) && Objects.equals(localTime, period.localTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, localTime);
    }

    @Override
    public int compareTo(Period o) {
        return this.localDate.compareTo(o.localDate);
    }
}
