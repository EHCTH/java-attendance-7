package attendance.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class Attendance {
    private final Name name;
    private final Periods periods;

    public Attendance(Name name, Periods periods) {
        this.name = name;
        this.periods = periods;
    }

    public Name getName() {
        return name;
    }

    public void checkIn(Period period) {
        validateIsSameDate(period);
        periods.save(period);
    }
    public void fills(LocalDate today) {
        periods.fills(today);

    }

    public Period modify(LocalDate localDate, LocalTime modifyTime) {
        return periods.modify(localDate, modifyTime);
    }

    public List<Period> sortedByList() {
        return periods.sortedByList();
    }

    public AttendanceStatistic statistic() {
        return periods.computeStatistic();
    }

    public Period findByDate(LocalDate localDate) {
        return periods.findByDate(localDate);
    }

    private void validateIsSameDate(Period period) {
        if (periods.isSameDate(period)) {
            throw new IllegalArgumentException("[ERROR] 이미 등록된 데이터가 존재합니다 수정메뉴를 이용해주세요");
        }
    }
}
