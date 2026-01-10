package attendance.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

public enum KoreanDayOfWeek {
    MONDAY(DayOfWeek.MONDAY, "월", "월요일"),
    TUESDAY(DayOfWeek.TUESDAY, "화", "화요일"),
    WEDNESDAY(DayOfWeek.WEDNESDAY, "수", "수요일"),
    THURSDAY(DayOfWeek.THURSDAY, "목", "목요일"),
    FRIDAY(DayOfWeek.FRIDAY, "금", "금요일"),
    SATURDAY(DayOfWeek.SATURDAY, "토", "토요일"),
    SUNDAY(DayOfWeek.SUNDAY, "일", "일요일");

    private final DayOfWeek dayOfWeek;
    private final String shortKor;
    private final String longKor;

    KoreanDayOfWeek(DayOfWeek dayOfWeek, String shortKor, String longKor) {
        this.dayOfWeek = dayOfWeek;
        this.shortKor = shortKor;
        this.longKor = longKor;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getShortKor() {
        return shortKor;
    }

    public String getLongKor() {
        return longKor;
    }

    public boolean isWeekDay() {
        return !isWeekend();
    }

    public boolean isWeekend() {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public static KoreanDayOfWeek findByDayOfWeek(DayOfWeek dayOfWeek) {
        return Arrays.stream(values())
                .filter(koreanDayOfWeek -> koreanDayOfWeek.dayOfWeek == dayOfWeek)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 요일입니다"));
    }

    public static KoreanDayOfWeek findByLocalDate(LocalDate localDate) {
        return findByDayOfWeek(localDate.getDayOfWeek());
    }
}
