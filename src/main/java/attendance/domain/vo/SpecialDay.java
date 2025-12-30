package attendance.domain.vo;

import java.time.LocalDate;
import java.util.List;

public class SpecialDay {
    private final List<LocalDate> specialDay;

    public SpecialDay(List<LocalDate> specialDay) {
        this.specialDay = specialDay;
    }
    public boolean contains(LocalDate localDate) {
        return specialDay.contains(localDate);
    }
}
