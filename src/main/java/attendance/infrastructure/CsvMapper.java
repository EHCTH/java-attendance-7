package attendance.infrastructure;

import attendance.domain.Attendance;
import attendance.domain.Name;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public Row toMap(List<String> data) {
        Name name = new Name(data.getFirst());
        LocalDateTime localDateTime = LocalDateTime.parse(data.getLast(), FORMATTER);
        return new Row(name, localDateTime);
    }
    public record Row(Name name, LocalDateTime localDateTime) {

    }
}
