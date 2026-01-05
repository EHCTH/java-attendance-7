package attendance.infrastrucutre;

import attendance.domain.Name;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvMapper {
    private static final int NAME_INDEX = 0;
    private static final int DATE_INDEX = 1;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Row toMap(List<String> data) {
        Name name = new Name(data.get(NAME_INDEX));
        LocalDateTime dateTime = LocalDateTime.parse(data.get(DATE_INDEX), FORMATTER);
        return new Row(name, dateTime);
    }
    public record Row(Name name, LocalDateTime localDateTime) {

    }
}
