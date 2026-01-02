package attendance.bootstrap;

import attendance.application.inbound.AttendanceUseCase;
import attendance.application.outbound.AttendanceRepository;
import attendance.application.service.AttendanceService;
import attendance.infrastrucutre.Mapper;
import attendance.infrastrucutre.MemoryAttendanceRepository;
import attendance.infrastrucutre.Parser;
import attendance.infrastrucutre.Reader;
import attendance.interfaces.inbound.Controller;
import attendance.interfaces.outbound.InputView;
import attendance.interfaces.outbound.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class AppConfig {
        private final LocalDate today = DateTimes.now().toLocalDate();
//    private final LocalDate today = LocalDate.of(2024, 12, 13);

    private final AttendanceRepository repository = new MemoryAttendanceRepository();
    private final AttendanceUseCase useCase = new AttendanceService(repository);
    private final Reader reader = new Reader(new Parser(), new Mapper());
    private final DataInitializer dataInitializer = new DataInitializer(repository, reader, today);
    public Controller controller() {
        dataInitializer.init();
        return new Controller(today, useCase, new InputView(), new OutputView());
    }


}
