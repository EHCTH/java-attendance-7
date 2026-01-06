package attendance.bootstrap;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.AttendanceRepository;
import attendance.application.service.AttendanceService;
import attendance.infrastructure.CsvMapper;
import attendance.infrastructure.CsvParser;
import attendance.infrastructure.CsvReader;
import attendance.infrastructure.MemoryAttendanceRepository;
import attendance.interfaces.adapter.inbound.Controller;
import attendance.interfaces.adapter.inbound.InputView;
import attendance.interfaces.adapter.inbound.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvWriter;

import java.time.LocalDate;

public class AppConfig {
    private final LocalDate today = DateTimes.now().toLocalDate();
//    private final LocalDate today = LocalDate.of(2024, 12, 13);
    private final AttendanceRepository repository = new MemoryAttendanceRepository();
    private final AttendanceUseCase useCase = new AttendanceService(repository);

    private final CsvMapper csvMapper = new CsvMapper();
    private final CsvParser csvParser = new CsvParser();
    private final CsvReader csvReader = new CsvReader(csvParser, csvMapper);
//    private final CsvWriter csvWriter = new CsvWriter();

    private final DataInitializer initializer = new DataInitializer(today, repository, csvReader);

    public Controller controller() {
        initializer.init();
        return new Controller(today, useCase, new InputView(), new OutputView());
    }
}
