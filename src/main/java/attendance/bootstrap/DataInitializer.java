package attendance.bootstrap;

import attendance.application.port.outbound.AttendanceRepository;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.domain.Period;
import attendance.domain.Periods;
import attendance.infrastructure.CsvMapper;
import attendance.infrastructure.CsvReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataInitializer {
    private final LocalDate today;
    private final AttendanceRepository repository;
    private final CsvReader csvReader;

    public DataInitializer(LocalDate today, AttendanceRepository repository, CsvReader csvReader) {
        this.today = today;
        this.repository = repository;
        this.csvReader = csvReader;
    }

    public void init() {
        List<CsvMapper.Row> rows = csvReader.readLine();
        Map<Name, Periods> namePeriodsMap = groupingByName(rows);

        List<Attendance> attendances = namePeriodsMap.entrySet()
                .stream()
                .map(this::toAttendance)
                .toList();


        attendances.forEach(x -> x.fills(today));
        attendances.forEach(repository::save);
//        List<Attendance> attendances = rows.stream()
//                .collect(Collectors.collectingAndThen(Collectors.groupingBy(
//                                CsvMapper.Row::name,
//                                Collectors.collectingAndThen(
//                                        Collectors.mapping(x -> Period.present(x.localDateTime()), Collectors.toList()),
//                                        Periods::new
//                                )),
//                        y -> y.entrySet()
//                                .stream()
//                                .map(entry -> new Attendance(entry.getKey(), entry.getValue()))
//                ))
//                .toList();

    }
    private Attendance toAttendance(Map.Entry<Name, Periods> entry) {
        return new Attendance(entry.getKey(), entry.getValue());
    }

    private Map<Name, Periods> groupingByName(List<CsvMapper.Row> rows) {
        return rows.stream()
                .collect(Collectors.groupingBy(
                        CsvMapper.Row::name,
                        Collectors.collectingAndThen(
                                Collectors.mapping(x -> Period.present(x.localDateTime()), Collectors.toList()),
                                Periods::new
                        )
                ));
    }


}
