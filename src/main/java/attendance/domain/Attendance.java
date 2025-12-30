package attendance.domain;

import attendance.domain.vo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Attendance {
    private static final Long LATE_TO_COUNT = 3L;
    private final Name name;
    private final PeriodRecords periodRecords;

    public Name getName() {
        return name;
    }

    public Attendance(Name name, PeriodRecords periodRecords) {
        this.name = name;
        this.periodRecords = periodRecords;
    }

    public Period findByDay(int day) {
        return periodRecords.findByDay(day);
    }

    public Period checkIn(LocalDate today, LocalTime localTime) {
        Period period = Period.present(today, localTime);
        return periodRecords.checkIn(period);
    }

    public Period modify(int day, LocalTime modifyTime) {
        return periodRecords.modify(day, modifyTime);
    }

    public void fillToAbsent(LocalDate past, LocalDate today, SpecialDay specialDay) {
        Predicate<Period> isExistRecord = periodRecords::isExistRecord;
        Predicate<LocalDate> isSpecialDay = specialDay::contains;
        past.datesUntil(today)
                .filter(isSpecialDay.negate())
                .map(Period::absent)
                .filter(Period::isWeekDay)
                .filter(isExistRecord.negate())
                .forEach(periodRecords::checkIn);
    }
    public AttendanceInfo info() {
        Map<Status, Long> statusLongMap = periodRecords.groupingByStatusAndCount();

        Long checkInCount = statusLongMap.getOrDefault(Status.CHECK_IN, 0L);
        Long lateCount = statusLongMap.getOrDefault(Status.LATE, 0L);
        Long absentCount = statusLongMap.getOrDefault(Status.ABSENT, 0L);

        Expire expire = Expire.findByThreshold(convertLateToAbsentCount(lateCount, absentCount));
        return new AttendanceInfo(checkInCount, absentCount, lateCount, expire);
    }
    public List<Period> records() {
        return periodRecords.stream().toList();
    }

    private long convertLateToAbsentCount(long late, long absent) {
        return absent + (late / LATE_TO_COUNT);
    }
}
