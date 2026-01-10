package attendance.bootstrap;

import attendance.application.port.outbound.Repository;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.domain.Records;
import attendance.domain.VisitDate;
import attendance.infrastructure.CsvMapper;
import attendance.infrastructure.CsvReader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataInit {
    private final LocalDate today;
    private final Repository<Name, Attendance> repository;
    private final CsvReader reader;

    public DataInit(LocalDate today, Repository<Name, Attendance> repository, CsvReader reader) {
        this.today = today;
        this.repository = repository;
        this.reader = reader;
    }

    public void init() {
        List<CsvMapper.Row> rows = reader.readLine();
        Map<Name, List<LocalDateTime>> collect = rows.stream()
                .collect(Collectors.groupingBy(
                        CsvMapper.Row::name,
                        Collectors.mapping(CsvMapper.Row::localDateTime, Collectors.toList())
                ));

        List<Attendance> attendances = collect.entrySet()
                .stream()
                .map(x -> new Attendance(
                        x.getKey(),
                        x.getValue()
                                .stream()
                                .map(VisitDate::present)
                                .collect(Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        Records::new
                                ))
                ))
                .toList();
        attendances.forEach(x -> x.fill(today));
        attendances.forEach(repository::save);
    }
}
