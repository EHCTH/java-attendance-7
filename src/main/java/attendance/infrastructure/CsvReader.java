package attendance.infrastructure;

import attendance.domain.Attendance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

public class CsvReader {
    private static final String PATH = "/attendances.csv";
    public static final int SKIP = 1;
    private final CsvParser csvParser;
    private final CsvMapper csvMapper;

    public CsvReader(CsvParser csvParser, CsvMapper csvMapper) {
        this.csvParser = csvParser;
        this.csvMapper = csvMapper;
    }

    public List<CsvMapper.Row> readLine() {
        InputStream resourceAsStream = CsvReader.class.getResourceAsStream(PATH);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("[ERROR] 해당 파일이 존재하지 않습니다");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            return br.lines()
                    .skip(SKIP)
                    .map(csvParser::parse)
                    .map(csvMapper::toMap)
                    .toList();

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 해당 파일이 존재하지 않습니다");
        }

    }
}
