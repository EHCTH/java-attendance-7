package attendance.infrastrucutre;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class Reader {
    private static final String PATH = "/attendances.csv";
    private static final int SKIP = 1;
    private final Parser parser;
    private final Mapper mapper;

    public Reader(Parser parser, Mapper mapper) {
        this.parser = parser;
        this.mapper = mapper;
    }

    public List<Mapper.Row> init() {
        return readLines();
    }

    public List<Mapper.Row> readLines() {
        InputStream inputStream = Reader.class.getResourceAsStream(PATH);
        if (inputStream == null) {
            throw new IllegalArgumentException("[ERROR] 파일이 존재하지 않습니다: " + PATH);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return br.lines()
                    .skip(SKIP)
                    .map(parser::parse)
                    .map(mapper::toMap)
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] csv 파일 읽기 실패: " + PATH, e);
        }
    }

    public static void main(String[] args) {
        Pattern pattern  = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}$");
        Pattern pattern1 = Pattern.compile("^[0-9]\\d*$");
        Pattern pattern2 = Pattern.compile("^[가-힣a-zA-Z-ㄱ-ㅎ]+$");

        System.out.println(pattern.matcher("02-412-4432").matches());
        System.out.println(pattern1.matcher("01213123").matches());
        System.out.println(pattern2.matcher("abcabc").matches());
    }


    public List<Mapper.Row> readeLinesV2() {
        InputStream inputStream = Reader.class.getResourceAsStream(PATH);
        if (inputStream == null) {
            throw new IllegalArgumentException(String.format("[ERROR] 현재 %s 가 존재하지 않습니다", PATH));
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return br.lines()
                    .skip(SKIP)
                    .map(parser::parse)
                    .map(mapper::toMap)
                    .toList();

        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("[ERROR] 현재 %s 가 존재하지 않습니다", PATH));

        }
    }

}
