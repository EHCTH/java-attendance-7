package attendance.domain;

import java.time.LocalDate;
import java.util.Arrays;

public enum HoliDay {
    CHRISTMAS(12, 25);
    private static final int YEAR = 2024;
    private final LocalDate localDate;

    HoliDay(int month, int day) {
        this.localDate = LocalDate.of(YEAR, month, day);
    }

    public static boolean isHoliday(LocalDate localDate) {
        return Arrays.stream(values())
                .anyMatch(holiDay -> holiDay.localDate.equals(localDate));
    }

}
