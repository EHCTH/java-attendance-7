package attendance.infrastrucutre;

import java.util.Arrays;
import java.util.List;

public class Parser {
    private static final String DELIMITER = ",";
    public List<String> parse(String data) {
        return Arrays.stream(data.split(DELIMITER)).toList();
    }
}
