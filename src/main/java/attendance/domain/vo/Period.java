package attendance.domain.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Period {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDate date;
    private final LocalTime time;

    private Period(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }
    public static Period absent(LocalDate localDate) {
        return new Period(localDate, null);
    }
    public static Period present(LocalDate localDate, LocalTime localTime) {
        return new Period(localDate, localTime);
    }
    public static Period parse(String data) {
        LocalDateTime localDateTime = LocalDateTime.parse(data, formatter);
        return present(localDateTime.toLocalDate(), localDateTime.toLocalTime());
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Period withTime(LocalTime modifyTime) {
        return new Period(this.date, modifyTime);
    }
    public boolean isWeekDay() {
        return korDayOfWeek().isWeekDay();
    }

    public int getDay() {
        return date.getDayOfMonth();
    }
    public boolean equalsToDate(Period period) {
        return this.date.equals(period.date);
    }

    public KorDayOfWeek korDayOfWeek() {
        return KorDayOfWeek.findByDayOfWeek(date.getDayOfWeek());
    }
    public Status computeStatus() {
        return korDayOfWeek().computeStatus(time);
    }
}
