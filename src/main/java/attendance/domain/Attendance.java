package attendance.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        past.datesUntil(today)
                .filter(this::isWeekDay)
                .filter(this::isNotSpecialDay)
                .filter(this::isNotExistRecord)
                .map(Period::absent)
                .forEach(records::checkIn);
    }

    public boolean isCheckIn(LocalDate localDate) {
        return records.isCheckIn(localDate);
    }

    public Period checkIn(Period period) {
        return records.checkIn(period);
    }

    public Period findByDay(LocalDate modifyDay) {
        return records.findByDay(modifyDay);
    }

    public Period modify(LocalDate modifyDay, LocalTime modifyTime) {
        return records.modify(modifyDay, modifyTime);
    }

    public List<AttendanceInfo> search() {
        return records.searchInfos();
    }

    public AttendanceSummary summarize() {
        return records.summarize();
    }

    private boolean isWeekDay(LocalDate localDate) {
        return !isWeekend(localDate);
    }

    private boolean isWeekend(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isNotSpecialDay(LocalDate localDate) {
        return !SpecialDay.isContainsSpecialDay(localDate);
    }

    private boolean isNotExistRecord(LocalDate localDate) {
        return !records.isCheckIn(localDate);
    }

}
