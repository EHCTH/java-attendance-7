package attendance.application.service;

import attendance.domain.Attendance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvParser {
    private static final int SKIP = 1;
    private static final String PATH = "/attendances.csv";
    private static final String DELIMITER = ",";
    private final RowMapper rowMapper;

    public CsvParser(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }
    // TODO PATH PARSE 하는 법 유심히 보자

    /*
    public List<Mapper.Row> parse() {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(PATH);

        if (inputStream == null) {
            throw new IllegalArgumentException("[ERROR] 리소스 파일을 찾을 수 없습니다: " + PATH);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(SKIP)
                    .map(this::splitToDelimiter)
                    .map(rowMapper::toMap)
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] CSV 파일을 읽을 수 없습니다: " + PATH, e);
        }
    }
     */

    public List<Mapper.Row> parse() {
        try (InputStream is = CsvParser.class.getResourceAsStream(PATH)) {
            if (is == null) {
                throw new IllegalArgumentException("not found: " + PATH);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .skip(SKIP)
                        .map(this::splitToDelimiter)
                        .map(rowMapper::toMap)
                        .toList();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] CSV 읽기 실패: " + PATH, e);
        }
    }

    private List<String> splitToDelimiter(String data) {
        return List.of(data.split(DELIMITER));
    }

}
