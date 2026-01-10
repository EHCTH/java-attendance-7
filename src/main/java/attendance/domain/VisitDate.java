package attendance.domain;

import java.time.*;

public class VisitDate implements Comparable<VisitDate>{
    private final LocalDate date;
    private final LocalTime time;

    private VisitDate(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }


    public static VisitDate present(LocalDateTime localDateTime) {
        return new VisitDate(localDateTime.toLocalDate(), localDateTime.toLocalTime());
    }

    public static VisitDate absent(LocalDate localDate) {
        return new VisitDate(localDate, null);
    }

    public static VisitDate checkIn(LocalDate localDate, LocalTime localTime) {
        return new VisitDate(localDate, localTime);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
    public VisitDate withTime(LocalTime modifyTime) {
        return new VisitDate(date, modifyTime);
    }

    public boolean isWeekDay() {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY;
    }

    public Status computeByDelay() {
        if (time == null) {
            return Status.ABSENT;
        }
        LectureTime lectureTime = LectureTime.findByLocalDate(date);
        Duration delay = lectureTime.computeByTime(time);
        return Status.computeByDelay(delay);
    }
    public boolean isSameDate(LocalDate other) {
        return this.date.equals(other);
    }

    @Override
    public int compareTo(VisitDate o) {
        return this.date.compareTo(o.date);
    }
}
