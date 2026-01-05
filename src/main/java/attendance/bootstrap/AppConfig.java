package attendance.bootstrap;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.AttendanceRepository;
import attendance.application.service.AttendanceService;
import attendance.infrastrucutre.*;
import attendance.interfaces.adapter.inbound.Controller;
import attendance.interfaces.adapter.outbound.InputView;
import attendance.interfaces.adapter.outbound.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class AppConfig {
//        private final LocalDate today = DateTimes.now().toLocalDate();
    private final LocalDate today = LocalDate.of(2024, 12, 13);
    private final AttendanceRepository repository = new MemoryAttendanceRepository();
    private final AttendanceUseCase useCase = new AttendanceService(repository);

    private final CsvMapper csvMapper = new CsvMapper();
    private final CsvParser csvParser = new CsvParser();
    private final CsvReader csvReader = new CsvReader(csvParser, csvMapper);
    private final CsvWriter csvWriter = new CsvWriter();

    private final DataInitializer initializer = new DataInitializer(today, repository, csvReader, csvWriter);

    public Controller controller() {
        initializer.init();
        return new Controller(today, new InputView(), new OutputView(), useCase, initializer);
    }

}

