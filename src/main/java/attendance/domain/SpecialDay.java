package attendance.domain;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Map;

public enum SpecialDay {
    크리스마스(12, 25);
    private final MonthDay monthDay;

    SpecialDay(int month, int day) {
        this.monthDay = MonthDay.of(month, day);
    }
    public static boolean isSpecial(LocalDate today) {
        MonthDay monthDay = MonthDay.from(today);
        return Arrays.stream(values())
                .anyMatch(x -> x.monthDay.equals(monthDay));
    }
}
