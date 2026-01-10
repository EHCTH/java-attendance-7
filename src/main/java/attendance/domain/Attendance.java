package attendance.domain;

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
    public void fill(LocalDate localDate) {
        records.fills(localDate);
    }

    public void checkIn(VisitDate visitDate) {
        records.checkIn(visitDate);
    }

    public VisitDate findByDate(LocalDate localDate) {
        return records.findByDate(localDate);
    }

    public VisitDate modify(LocalDate localDate, LocalTime modifyTime) {
        return records.modify(localDate, modifyTime);
    }

    public List<VisitDate> sortedVisitDate() {
        return records.sortedVisitDates();
    }

    public ExpireStatistic expireStatistic() {
        return records.expireStatistic();
    }


}

