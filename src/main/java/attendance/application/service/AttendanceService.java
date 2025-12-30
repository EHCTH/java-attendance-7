package attendance.application.service;

import attendance.application.inbound.AttendanceUseCase;
import attendance.application.outbound.AttendanceRepository;
import attendance.domain.Attendance;
import attendance.domain.Name;
import attendance.domain.vo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceService implements AttendanceUseCase {
    private final LocalDate today;
    private final SpecialDay specialDay;
    private final AttendanceRepository repository;

    public AttendanceService(LocalDate today, SpecialDay specialDay, AttendanceRepository repository) {
        this.today = today;
        this.specialDay = specialDay;
        this.repository = repository;
    }

    @Override
    public ResponseAttendance checkIn(Name name, LocalTime localTime) {
        Attendance attendance = repository.findByName(name);
        Period period = attendance.checkIn(today, localTime);
        return createResponseAttendance(name, period);
    }

    @Override
    public List<ResponseAttendance> modify(Name name, int day, LocalTime modifyTime) {
        Attendance attendance = repository.findByName(name);
        Period before = attendance.findByDay(day);
        Period after = attendance.modify(day, modifyTime);
        return List.of(
                createResponseAttendance(name, before),
                createResponseAttendance(name, after)
        );
    }

    @Override
    public ResponseAttendanceRecords search(Name name) {
        Attendance attendance = repository.findByName(name);

        List<ResponseAttendance> records = attendance.records()
                .stream()
                .map(period -> createResponseAttendance(name, period))
                .sorted()
                .toList();

        AttendanceInfo info = attendance.info();

        return new ResponseAttendanceRecords(
                records,
                info.present(),
                info.late(),
                info.absent(),
                info.expire()
        );
    }

    @Override
    public List<ResponseExpire> expire() {
        List<Attendance> attendances = repository.findAll();
        return attendances.stream()
                .map(this::createResponseExpire)
                .filter(ResponseExpire::isExpire)
                .sorted()
                .toList();
    }

    @Override
    public void validateContainsName(Name name) {
        if (isNotExistName(name)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
        }
    }

    @Override
    public void validateDate() {
        if (specialDay.contains(today) || isWeekend()) {
            KorDayOfWeek korDayOfWeek = KorDayOfWeek.findByDayOfWeek(today.getDayOfWeek());
            String errorMessage = String.format("[ERROR] %d월 %02d일 %s은 등교일이 아닙니다.",
                    today.getMonthValue(),
                    today.getDayOfMonth(),
                    korDayOfWeek.getDisplay()
            );
            throw new IllegalArgumentException(errorMessage);
        }
    }
    private boolean isNotExistName(Name name) {
        return !repository.containsKey(name);
    }
    private boolean isWeekend() {
        return !KorDayOfWeek.findByDayOfWeek(today.getDayOfWeek()).isWeekDay();
    }

    private ResponseExpire createResponseExpire(Attendance attendance) {
        AttendanceInfo info = attendance.info();
        return new ResponseExpire(
                attendance.getName(),
                info.absent(),
                info.late(),
                info.expire()
        );
    }
    private ResponseAttendance createResponseAttendance(Name name, Period period) {
        Status status = period.computeStatus();
        return new ResponseAttendance(name, period, status);
    }
}
