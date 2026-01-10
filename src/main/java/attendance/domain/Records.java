package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Records {
    private final List<VisitDate> visitDates;

    public Records(List<VisitDate> visitDates) {
        this.visitDates = visitDates;
    }


    public void fills(LocalDate today) {
        LocalDate start = today.with(TemporalAdjusters.firstDayOfMonth());
        start.datesUntil(today)
                .map(VisitDate::absent)
                .filter(this::isNotContains)
                .filter(this::isNotSpecialDay)
                .filter(this::isWeekDay)
                .forEach(this::add);
    }

    public void checkIn(VisitDate visitDate) {
        add(visitDate);
    }

    public VisitDate findByDate(LocalDate date) {
        return visitDates.stream()
                .filter(x -> x.isSameDate(date))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 등록된 정보가 없습니다"));
    }

    public VisitDate modify(LocalDate localDate, LocalTime modifyTime) {
        VisitDate before = findByDate(localDate);
        VisitDate after = before.withTime(modifyTime);
        removeIf(before);
        add(after);
        return after;

    }

    public List<VisitDate> sortedVisitDates() {
        return visitDates.stream()
                .sorted()
                .toList();
    }

    public ExpireStatistic expireStatistic() {
        Map<Status, Long> statusLongMap = statusGroupingCount();
        Long absent = statusLongMap.getOrDefault(Status.ABSENT, 0L);
        Long late = statusLongMap.getOrDefault(Status.LATE, 0L);
        Long present = statusLongMap.getOrDefault(Status.SAFE, 0L);
        Long convertToAbsent = convertToAbsent(absent, late);
        Expire expire = Expire.findByAbsentCount(convertToAbsent);
        return new ExpireStatistic(absent, late, present, expire);

    }

    private Map<Status, Long> statusGroupingCount() {
        return visitDates.stream()
                .map(VisitDate::computeByDelay)
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                );
    }

    private void add(VisitDate visitDate) {
        validateDuplicate(visitDate);
        visitDates.add(visitDate);
    }

    private void removeIf(VisitDate visitDate) {
        visitDates.removeIf(x -> x.isSameDate(visitDate.getDate()));
    }

    private Long convertToAbsent(long absent, long late) {
        return (late / 3) + absent;
    }

    private void validateDuplicate(VisitDate visitDate) {
        if (visitDates.stream().anyMatch(x -> x.isSameDate(visitDate.getDate()))) {
            throw new IllegalArgumentException("[ERROR] 이미 등록된 정보입니다");
        }
    }
    private boolean isNotContains(VisitDate other) {
        return visitDates.stream().noneMatch(x -> x.isSameDate(other.getDate()));
    }
    private boolean isNotSpecialDay(VisitDate other) {
        return !HoliDay.contains(other.getDate());
    }
    private boolean isWeekDay(VisitDate other) {
        return other.isWeekDay();
    }


}
