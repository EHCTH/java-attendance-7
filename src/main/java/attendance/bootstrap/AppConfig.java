package attendance.bootstrap;

import attendance.application.inbound.AttendanceUseCase;
import attendance.application.outbound.AttendanceRepository;
import attendance.application.service.AttendanceService;
import attendance.application.service.CsvParser;
import attendance.application.service.RowMapper;
import attendance.domain.Attendance;
import attendance.domain.vo.Period;
import attendance.domain.vo.SpecialDay;
import attendance.infrastrucutre.MemoryAttendanceRepository;
import attendance.intrefaces.inbound.AttendanceController;
import attendance.intrefaces.outbound.InputView;
import attendance.intrefaces.outbound.OutputView;
import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class AppConfig {
        private final LocalDate today = DateTimes.now().toLocalDate();
//    private final LocalDate today = LocalDate.of(2024, 12, 13);

    private final SpecialDay specialDay = new SpecialDay(List.of(LocalDate.of(2024, 12, 25)));
    private final CsvParser csvParser = new CsvParser(new RowMapper());

    private final AttendanceRepository attendanceRepository = new MemoryAttendanceRepository();
    private final AttendanceUseCase attendanceUseCase = new AttendanceService(today, specialDay,attendanceRepository);
    private final DataInitializer dataInitializer = new DataInitializer(today, specialDay, attendanceRepository, csvParser);

    public AttendanceController controller() {
        dataInitializer.init();
//        for (Attendance attendance : attendanceRepository.findAll()) {
//            System.out.println(attendance.getName().value());
//            attendance.records()
//                    .stream()
//                    .sorted(Comparator.comparing(Period::getDate))
//                    .forEach(x -> System.out.println(x.getDate() + " " + x.getTime()));
//            System.out.println();
//        }

        return new AttendanceController(today, attendanceUseCase, new InputView(), new OutputView());
    }
}
