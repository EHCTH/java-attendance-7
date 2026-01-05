package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Records {
    private final List<Period> periods;

    public Records(List<Period> periods) {
        this.periods = new ArrayList<>(periods);
    }

    public Period checkIn(Period period) {
        validateExistsCheckInfo(period.getLocalDate());
        periods.add(period);
        return period;
    }

    public Period modify(LocalDate modifyDay, LocalTime modifyTime) {
        Period before = findByDay(modifyDay);
        Period after = before.withTime(modifyTime);

        removeIf(before);
        periods.add(after);

        return after;
    }

    public List<AttendanceInfo> searchInfos() {
        return sortedPeriods()
                .stream()
                .map(period -> new AttendanceInfo(period, period.computeStatus()))
                .toList();
    }

    public Map<Status, Long> groupingByStatusCount() {
        return periods.stream()
                .map(Period::computeStatus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Period findByDay(LocalDate modifyDay) {
        return periods.stream()
                .filter(period -> period.isSameDate(modifyDay))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 현재 그런 정보는 등록되지 않았습니다"));
    }

    public AttendanceSummary summarize() {
        Map<Status, Long> statusLongMap = groupingByStatusCount();
        long absent = statusLongMap.getOrDefault(Status.ABSENT, 0L);
        long late = statusLongMap.getOrDefault(Status.LATE, 0L);
        long present = statusLongMap.getOrDefault(Status.PRESENT, 0L);
        Expire expire = Expire.computeByThreshold(Expire.toAbsentCount(absent, late));
        return new AttendanceSummary(absent, late, present, expire);
    }

    public boolean isCheckIn(LocalDate localDate) {
        return periods.stream()
                .map(Period::getLocalDate)
                .anyMatch(x -> x.equals(localDate));
    }

    private List<Period> sortedPeriods() {
        return periods.stream()
                .sorted()
                .toList();
    }

    private void removeIf(Period period) {
        periods.removeIf(x -> x.isSameDate(period.getLocalDate()));
    }

    private void validateExistsCheckInfo(LocalDate localDate) {
        if (isCheckIn(localDate)) {
            throw new IllegalArgumentException("[ERROR] 이미 등록된 정보입니다");
        }
    }

}
