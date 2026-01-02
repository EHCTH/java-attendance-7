package attendance.infrastrucutre;

import attendance.domain.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mapper {
    private static final int NAME_INDEX = 0;
    private static final int DATE_INDEX = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public record Row(Name name, LocalDateTime localDateTime) {

    }
    public Row toMap(List<String> data) {
        Name name = new Name(data.get(NAME_INDEX));
        LocalDateTime localDateTime = LocalDateTime.parse(data.get(DATE_INDEX), formatter);
        return new Row(name, localDateTime);
    }
}
