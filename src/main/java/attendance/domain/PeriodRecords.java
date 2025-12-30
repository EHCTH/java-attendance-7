package attendance.domain;

import attendance.domain.vo.Period;
import attendance.domain.vo.Status;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PeriodRecords {
    private final List<Period> records;

    public PeriodRecords(List<Period> records) {
        this.records = records;
    }

    public Period checkIn(Period period) {
        validate(period);
        records.add(period);
        return period;
    }

    public Period modify(int day, LocalTime modifyTime) {
        Period before = findByDay(day);
        Period after = before.withTime(modifyTime);
        removeIf(before);
        return checkIn(after);
    }

    public boolean isExistRecord(Period other) {
        return records.stream().anyMatch(other::equalsToDate);
    }

    public Period findByDay(int day) {
        Predicate<Period> predicateDay = x -> x.getDay() == day;
        return records.stream()
                .filter(predicateDay)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당날짜는 존재하지 않습니다"));
    }

    public Map<Status, Long> groupingByStatusAndCount() {
        return records.stream()
                .map(Period::computeStatus)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    }
    public Stream<Period> stream() {
        return records.stream();
    }

    private void validate(Period period) {
        if (isExistRecord(period)) {
            throw new IllegalArgumentException("[ERROR] 현재 정보가 등록되어있습니다 수정을 이용해주세요");
        }
    }

    private void removeIf(Period period) {
        records.removeIf(period::equalsToDate);
    }
}
