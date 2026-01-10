package attendance.infrastructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvReader {
    private static final String PATH = "/attendances.csv";
    public static final int SKIP = 1;
    private final CsvParser parser;
    private final CsvMapper mapper;

    public CsvReader(CsvParser parser, CsvMapper mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }
    public List<CsvMapper.Row> readLine() {
        InputStream resourceAsStream = CsvReader.class.getResourceAsStream(PATH);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("[ERROR] 그런 파일은 존재하지 않습니다");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            return br.lines()
                    .skip(SKIP)
                    .map(parser::parse)
                    .map(mapper::toMap)
                    .toList();

        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 그런 파일은 존재하지 않습니다");
        }

    }
}
