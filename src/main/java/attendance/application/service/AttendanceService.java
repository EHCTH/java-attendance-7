package attendance.application.service;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.AttendanceRepository;
import attendance.domain.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceService implements AttendanceUseCase {
    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response checkIn(Name name, LocalDate today, LocalTime localTime) {
        validateTimeOut(localTime);
        Attendance attendance = repository.findByName(name);
        Period period = Period.checkIn(today, localTime);
        attendance.checkIn(period);
        return createResponse(period);
    }

    @Override
    public ResponseModify modify(Name name, LocalDate localDate, LocalTime modifyTime) {
        validateTimeOut(modifyTime);
        Attendance attendance = repository.findByName(name);
        Period before = attendance.findByDate(localDate);
        Period after = attendance.modify(localDate, modifyTime);
        return new ResponseModify(
                createResponse(before),
                createResponse(after)
        );
    }

    @Override
    public ResponseSearch search(Name name) {
        Attendance attendance = repository.findByName(name);
        List<Response> responses = attendance.sortedByList()
                .stream()
                .map(this::createResponse)
                .toList();
        AttendanceStatistic statistic = attendance.statistic();
        return new ResponseSearch(name, responses, statistic);

    }

    @Override
    public List<ResponseExpire> expire() {
        return repository.findAll()
                .stream()
                .map(this::createResponseExpire)
                .filter(ResponseExpire::isExpire)
                .sorted()
                .toList();
    }

    @Override
    public void validateIncludeName(Name name) {
        if (!repository.containsKey(name)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
        }
    }
    private void validateTimeOut(LocalTime localTime) {
        if (!CampusTime.isWithin(localTime)) {
            throw new IllegalArgumentException("[ERROR] 캠퍼스 운영시간이 아닙니다");
        }
    }

    private Response createResponse(Period period) {
        return new Response(period, period.computeStatus());
    }
    private ResponseExpire createResponseExpire(Attendance attendance) {
        return new ResponseExpire(attendance.getName(), attendance.statistic());
    }

}
