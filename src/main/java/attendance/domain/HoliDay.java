package attendance.domain;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

public enum HoliDay {
    크리스마스(12, 25);
    private final MonthDay monthDay;

    HoliDay(int month, int day) {
        this.monthDay = MonthDay.of(month, day);
    }

    public static boolean contains(LocalDate localDate) {
        MonthDay month = MonthDay.from(localDate);
        return Arrays.stream(values()).anyMatch(x -> x.monthDay.equals(month));
    }
}
