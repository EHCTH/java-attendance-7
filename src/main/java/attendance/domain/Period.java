package attendance.domain;

import java.nio.file.Path;
import java.time.*;

public class Period implements Comparable<Period> {
    private final LocalDate localDate;
    private final LocalTime localTime;

    private Period(LocalDate localDate, LocalTime localTime) {
        this.localDate = localDate;
        this.localTime = localTime;
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

    public Period withTime(LocalTime modifyTime) {
        return new Period(this.localDate, modifyTime);
    }
    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public boolean isSameDate(Period period) {
        return isSameDate(period.getLocalDate());
    }

    public boolean isSameDate(LocalDate localDate) {
        return this.localDate.equals(localDate);
    }

    public boolean isWeekDay() {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    public boolean isNotSpecialDay() {
        return !SpecialDay.isSpecial(localDate);
    }
    public StatusType computeStatus() {
        if (localTime == null) {
            return StatusType.ABSENT;
        }
        LectureTime lectureTime = LectureTime.findByDate(localDate);
        Duration dealy = lectureTime.computeDelayByTime(localTime);
        return StatusType.computeByDurationDelay(dealy);
    }

    @Override
    public int compareTo(Period o) {
        return this.localDate.compareTo(o.localDate);
    }
}
