package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Records {
    private final List<Period> records;

    public Records(List<Period> records) {
        this.records = records;
    }

    public void add(Period period) {
        records.add(period);
    }

    public List<Period> getRecords() {
        return records;
    }

    public void fill(LocalDate past, LocalDate today) {
        past.datesUntil(today)
                .map(Period::absent)
                .filter(Period::isWeekDay)
                .filter(this::isNotContains)
                .filter(this::isNotContainsSpecialDay)
                .forEach(this::add);
    }

    public Period findByDay(int day) {
        return records.stream()
                .filter(period -> period.isDay(day))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 현재 그런 날짜는 존재하지 않습니다"));
    }

    public Period modify(int day, LocalTime localTime) {
        Period before = findByDay(day);
        records.removeIf(before::equals);
        Period after = before.withTime(localTime);
        add(after);
        return after;
    }

    public boolean isNotContains(Period period) {
        return records.stream()
                .map(Period::getLocalDate)
                .noneMatch(period::equalsToDate);
    }

    private boolean isNotContainsSpecialDay(Period period) {
        return !HoliDay.isHoliday(period.getLocalDate());
    }

}
