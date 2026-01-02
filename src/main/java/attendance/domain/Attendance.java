package attendance.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Attendance {
    private final Name name;
    private final Records records;

    public Attendance(Name name, Records records) {
        this.name = name;
        this.records = records;
    }

    public Name getName() {
        return name;
    }

    public void fills(LocalDate past, LocalDate today) {
        records.fill(past, today);
    }

    public Period findByDay(int day) {
        return records.findByDay(day);
    }

    public Period checkIn(Period period) {
        validateExistCheckIn(period);
        records.add(period);
        return period;
    }

    public Period modify(int day, LocalTime localTime) {
        return records.modify(day, localTime);
    }


    public List<Period> search() {
        return records.getRecords()
                .stream()
                .sorted()
                .toList();
    }

    public Map<Status, Long> groupingByStatusAndCount() {
        return records.getRecords()
                .stream()
                .map(Period::computeStatus)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }

    private void validateExistCheckIn(Period period) {
        if (isContains(period)) {
            throw new IllegalArgumentException("[ERROR] 현재 정보가 저장되어 있습니다 수정 옵션을 이용해주세요");
        }

    }

    private boolean isContains(Period period) {
        return !records.isNotContains(period);
    }
}
