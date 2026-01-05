package attendance.bootstrap;

import attendance.application.port.outbound.AttendanceRepository;
import attendance.domain.*;
import attendance.infrastrucutre.CsvMapper;
import attendance.infrastrucutre.CsvReader;
import attendance.infrastrucutre.CsvWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DataInitializer {
    private final LocalDate today;
    private final AttendanceRepository repository;
    private final CsvReader csvReader;
    private final CsvWriter csvWriter;

    public DataInitializer(LocalDate today, AttendanceRepository repository, CsvReader csvReader, CsvWriter csvWriter) {
        this.today = today;
        this.repository = repository;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
    }

    public void init() {
        LocalDate past = today.with(TemporalAdjusters.firstDayOfMonth());
        Map<Name, List<LocalDateTime>> nameListMap = groupingByName();
        List<Attendance> attendances = nameListMap.entrySet()
                .stream()
                .map(this::createAttendance)
                .toList();

        attendances.forEach(attendance -> attendance.fills(past, today));
        attendances.forEach(repository::save);

        // TODO 테스트
        List<Attendance> collect = csvReader.read().stream()
                .collect(toAttendances());

    }
    public void fetch() {
        List<String> lines = repository.findAll()
                .stream()
                .flatMap(a -> createWriteData(a).stream())
                .toList();

        csvWriter.appendWithHeaderIfNew(
                "attendances-generated.csv",
                "name,datetime",
                lines
        );

    }

    private List<String> createWriteData(Attendance attendance) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Name name = attendance.getName();
        List<AttendanceInfo> search = attendance.search();

        List<String> list = search.stream()
                .map(AttendanceInfo::period)
                .filter(x -> x.getLocalTime() != null)
                .map(x -> LocalDateTime.of(x.getLocalDate(), x.getLocalTime()))
                .map(x -> x.format(dateTimeFormatter))
                .toList();

        return list.stream()
                .map(x -> name.getName() + "," + x)
                .toList();


    }

    private Attendance createAttendance(Map.Entry<Name, List<LocalDateTime>> entrySet) {
        Records records = entrySet.getValue()
                .stream()
                .map(Period::present)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Records::new));

        return new Attendance(entrySet.getKey(), records);
    }


    private Map<Name, List<LocalDateTime>> groupingByName() {
        return csvReader.read()
                .stream()
                .collect(Collectors.groupingBy(
                        CsvMapper.Row::name,
                        Collectors.mapping(
                                CsvMapper.Row::localDateTime,
                                Collectors.toList()
                        )
                ));
    }

    private Collector<CsvMapper.Row, ?, List<Attendance>> toAttendances() {
        return Collectors.collectingAndThen(
                Collectors.groupingBy(
                        CsvMapper.Row::name,
                        Collectors.collectingAndThen(
                                Collectors.mapping(r -> Period.present(r.localDateTime()), Collectors.toList()),
                                Records::new
                        )
                ),
                map -> map.entrySet().stream()
                        .map(e -> new Attendance(e.getKey(), e.getValue()))
                        .toList()
        );
    }
}
