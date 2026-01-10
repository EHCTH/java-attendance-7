package attendance.bootstrap;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.Repository;
import attendance.application.service.AttendanceService;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.infrastructure.CsvMapper;
import attendance.infrastructure.CsvParser;
import attendance.infrastructure.CsvReader;
import attendance.infrastructure.MemoryRepository;
import attendance.interfaces.adapter.inbound.Controller;
import attendance.interfaces.adapter.inbound.InputView;
import attendance.interfaces.adapter.inbound.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class AppConfig {
    private final LocalDate localDate = DateTimes.now().toLocalDate();
    private final Repository<Name, Attendance> repository = new MemoryRepository();
    private final DataInit dataInit = new DataInit(localDate, repository, new CsvReader(new CsvParser(), new CsvMapper()));
    private final AttendanceUseCase useCase = new AttendanceService(localDate, repository);
    public Controller controller() {
        dataInit.init();
        return new Controller(useCase, new InputView(localDate), new OutputView());
    }
}
