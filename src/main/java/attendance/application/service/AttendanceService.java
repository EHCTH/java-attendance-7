package attendance.application.service;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.AttendanceRepository;
import attendance.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceService implements AttendanceUseCase {
    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response checkIn(Name name, LocalDate localDate, LocalTime localTime) {
        validateCampusTime(localTime);
        Attendance attendance = findByName(name);

        Period period = Period.checkIn(localDate, localTime);
        Period checkIn = attendance.checkIn(period);

        return createResponse(checkIn, checkIn.computeStatus());
    }


    @Override
    public ResponseModify modify(Name name, LocalDate modifyDay, LocalTime modifyTime) {
        validateCampusTime(modifyTime);
        Attendance attendance = findByName(name);

        Period before = attendance.findByDay(modifyDay);
        Period after = attendance.modify(modifyDay, modifyTime);

        return createResponseModify(before, after);
    }

    @Override
    public ResponseSearch search(Name name) {
        Attendance attendance = findByName(name);

        List<Response> responses = attendance.search()
                .stream()
                .map(this::createResponse)
                .toList();

        return createResponseSearch(name, responses, attendance.summarize());
    }

    @Override
    public List<ResponseExpire> expire() {
        List<Attendance> attendances = repository.findAll();
        return attendances.stream()
                .map(this::createResponseExpire)
                .filter(ResponseExpire::isExpire)
                .sorted(ResponseExpire.order())
                .toList();
    }

    @Override
    public void validateExistName(Name name) {
        if (!repository.containsKey(name)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
        }
    }

    private Response createResponse(AttendanceInfo attendanceInfo) {
        return createResponse(attendanceInfo.period(), attendanceInfo.status());
    }

    private Response createResponse(Period period, Status status) {
        return new Response(period, status);
    }

    private ResponseModify createResponseModify(Period before, Period after) {
        return new ResponseModify(
                createResponse(before, before.computeStatus()),
                createResponse(after, after.computeStatus())
        );
    }

    private ResponseSearch createResponseSearch(Name name, List<Response> responses, AttendanceSummary summary) {
        return new ResponseSearch(name, responses, summary);
    }

    private ResponseExpire createResponseExpire(Attendance attendance) {
        return new ResponseExpire(attendance.getName(), attendance.summarize());
    }

    private Attendance findByName(Name name) {
        return repository.findByName(name);
    }



    private void validateCampusTime(LocalTime localtime) {
        if (!CampusTime.isOpen(localtime)) {
            throw new IllegalArgumentException("[ERROR] 현재 캠퍼스는 닫혀있습니다");
        }
    }
}
