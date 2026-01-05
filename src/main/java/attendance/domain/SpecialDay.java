package attendance.domain;

import java.time.LocalDate;
import java.util.Arrays;

public enum SpecialDay {
    크리스마스(12, 25),;
    private final LocalDate localDate;

    SpecialDay(int month, int day) {
        this.localDate = LocalDate.of(2024, month, day);
    }
    public static boolean isContainsSpecialDay(LocalDate today) {
        return Arrays.stream(values())
                .anyMatch(x -> x.localDate.equals(today));
    }
}
