package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Periods {
    private final List<Period> periods;

    public Periods(List<Period> periods) {
        this.periods = periods;
    }
    public void fills(LocalDate today) {
        LocalDate past = today.with(TemporalAdjusters.firstDayOfMonth());
        past.datesUntil(today)
                .map(Period::absent)
                .filter(x -> !isSameDate(x))
                .filter(Period::isWeekDay)
                .filter(Period::isNotSpecialDay)
                .forEach(this::save);
    }

    public void save(Period period) {
        periods.add(period);
    }

    public Period modify(LocalDate localDate, LocalTime modifyTime) {
        Period before = findByDate(localDate);
        periods.removeIf(x -> x.isSameDate(before));
        Period after = before.withTime(modifyTime);
        save(after);
        return after;
    }


    public boolean isSameDate(Period period) {
        return periods.stream().anyMatch(period::isSameDate);
    }

    public Period findByDate(LocalDate localDate) {
        return periods.stream()
                .filter(x -> x.isSameDate(localDate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 현재 그런 데이터는 없습니다"));
    }

    public List<Period> sortedByList() {
        return periods.stream()
                .sorted()
                .toList();
    }

    public AttendanceStatistic computeStatistic() {
        Map<StatusType, Long> statusTypeLongMap = periods.stream()
                .collect(Collectors.groupingBy(Period::computeStatus, Collectors.counting()));
        return toStatistic(statusTypeLongMap);

    }

    private AttendanceStatistic toStatistic(Map<StatusType, Long> statusTypeLongMap) {
        long absent = statusTypeLongMap.getOrDefault(StatusType.ABSENT, 0L);
        long late = statusTypeLongMap.getOrDefault(StatusType.LATE, 0L);
        long safe = statusTypeLongMap.getOrDefault(StatusType.SAFE, 0L);
        ExpireType expireType = ExpireType.computeByPenalty(ExpireType.convertAbsentCount(absent, late));
        return new AttendanceStatistic(absent, late, safe, expireType);
    }
}
