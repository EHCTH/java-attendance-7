package attendance.bootstrap;

import attendance.application.outbound.AttendanceRepository;
import attendance.application.service.CsvParser;
import attendance.application.service.Mapper;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.domain.PeriodRecords;
import attendance.domain.vo.Period;
import attendance.domain.vo.SpecialDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
/*
  TODO 내일 해야 할 목록
  1. Collections.mapping 잘 사용해보기
  2. PATH PARSE 하는 법 유심히 보자 (InputStream)
  3. LocalDateTime 등 날짜 및 시간 유틸 공부하자
  4. Regex 정규식 공부하자

  이렇게만 하면 충분하다
 */

public class DataInitializer {
    private final LocalDate today;
    private final SpecialDay specialDay;
    private final AttendanceRepository repository;
    private final CsvParser csvParser;

    public DataInitializer(LocalDate today, SpecialDay specialDay, AttendanceRepository repository, CsvParser csvParser) {
        this.today = today;
        this.specialDay = specialDay;
        this.repository = repository;
        this.csvParser = csvParser;
    }

    public void init() {

        List<Attendance> attendances = groupingByName()
                .entrySet()
                .stream()
                .map(this::createAttendance)
                .toList();

        attendances.stream()
                .peek(this::fills)
                .forEach(repository::save);
    }

    private void fills(Attendance attendance) {
        attendance.fillToAbsent(LocalDate.of(2024, 12, 1), today, specialDay);
    }

    private Map<String, List<String>> groupingByName() {
        return csvParser.parse()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Mapper.Row::name,
                                // TODO 여기 부분 중요 Collectors.mapping();
                                Collectors.mapping(Mapper.Row::dateData, Collectors.toList())
                        ));
    }

    private Attendance createAttendance(Map.Entry<String, List<String>> entry) {
        Name name = new Name(entry.getKey());
        PeriodRecords periodRecords = entry.getValue()
                .stream()
                .map(Period::parse)
                .collect(Collectors.collectingAndThen(Collectors.toList(), PeriodRecords::new));
        return new Attendance(name, periodRecords);
    }

    public static void main(String[] args) {
        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse("2025-12-09 11:11",input);

        DateTimeFormatter output = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 HH:mm 입니다", Locale.KOREA);


        System.out.println(localDateTime.format(output));
        System.out.println(localDateTime.getDayOfMonth());
        System.out.println(localDateTime.getDayOfWeek());
        System.out.println(localDateTime);

        System.out.printf("%nSystem.out.printf(%s ,%02d)%n", "hello world", 5);
        String hello = "hello";
        System.out.println(hello);
    }
}
