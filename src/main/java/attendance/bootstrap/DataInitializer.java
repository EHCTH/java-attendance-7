package attendance.bootstrap;

import attendance.application.outbound.AttendanceRepository;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.domain.Period;
import attendance.domain.Records;
import attendance.infrastrucutre.Mapper;
import attendance.infrastrucutre.Parser;
import attendance.infrastrucutre.Reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataInitializer {
    public static final LocalDate PAST = LocalDate.of(2024, 12, 1);
    private final AttendanceRepository repository;
    private final Reader reader;
    private final LocalDate today;

    /*
  TODO 내일 해야 할 목록
    1. Collections.mapping 잘 사용해보기
    2. PATH PARSE 하는 법 유심히 보자 (InputStream)
    3. LocalDateTime 등 날짜 및 시간 유틸 공부하자
    4. Regex 정규식 공부하자

    이렇게만 하면 충분하다
 */

    public DataInitializer(AttendanceRepository repository, Reader reader, LocalDate today) {
        this.repository = repository;
        this.reader = reader;
        this.today = today;
    }

    public void init() {
        List<Mapper.Row> rows = reader.init();

        Map<Name, List<LocalDateTime>> collect = groupingByName(rows);

        List<Attendance> attendances = collect.entrySet()
                .stream()
                .map(this::createAttendance)
                .toList();


        attendances.forEach(x -> x.fills(PAST, today));

        attendances.forEach(repository::save);
    }
    private Attendance createAttendance(Map.Entry<Name, List<LocalDateTime>> entry) {
        Records records = entry.getValue()
                .stream()
                .map(Period::present)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Records::new));
        return new Attendance(entry.getKey(), records);
    }

    private static Map<Name, List<LocalDateTime>> groupingByName(List<Mapper.Row> rows) {
        return rows.stream()
                .collect(Collectors.groupingBy(
                        Mapper.Row::name,
                        Collectors.mapping(Mapper.Row::localDateTime, Collectors.toList())));
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[1-9]\\d*");
        System.out.println(pattern.matcher("1").matches());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E요일", Locale.KOREA);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDate now = LocalDate.now();
        System.out.println(now.format(formatter));

        LocalDateTime localDateTime = LocalDateTime.parse("2024-05-15 12:13", dateTimeFormatter);
        String hello = "hello";

        String clear = "clear";
        System.out.println(clear);
        String helloWorld = "hello world";
        String hello2 = "hello2";

        System.out.println(hello);

        System.out.printf("hello");

        System.out.println(helloWorld);

        System.out.println(localDateTime);

    }

}
