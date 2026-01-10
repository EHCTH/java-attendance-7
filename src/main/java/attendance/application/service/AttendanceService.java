package attendance.application.service;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.application.port.outbound.Repository;
import attendance.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AttendanceService implements AttendanceUseCase {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MM월 dd일 EEEE", Locale.KOREA);
    private final LocalDate today;
    private final Repository<Name, Attendance> repository;

    public AttendanceService(LocalDate today, Repository<Name, Attendance> repository) {
        this.today = today;
        this.repository = repository;
    }

    @Override
    public Response checkIn(Name name, LocalTime localTime) {
        validateTime(localTime);

        Attendance attendance = findByName(name);
        VisitDate visitDate = VisitDate.checkIn(today, localTime);

        attendance.checkIn(visitDate);
        return new Response(visitDate);
    }


    @Override
    public ResponseModify modify(Name name, LocalDate localDate, LocalTime localTime) {
        validateTime(localTime);
        validateName(name);

        Attendance attendance = findByName(name);

        VisitDate before = attendance.findByDate(localDate);
        VisitDate after = attendance.modify(localDate, localTime);
        return new ResponseModify(
                new Response(before),
                new Response(after)
        );

    }

    @Override
    public ResponseSearch search(Name name) {
        validateName(name);
        Attendance attendance = findByName(name);

        List<VisitDate> visitDates = attendance.sortedVisitDate();
        ExpireStatistic expireStatistic = attendance.expireStatistic();

        List<Response> responses = visitDates.stream()
                .map(Response::new)
                .toList();
        return new ResponseSearch(
                name,
                responses,
                expireStatistic
        );

    }

    @Override
    public List<ResponseExpireStatistic> expire() {
        return repository.findAll()
                .stream()
                .map(x -> new ResponseExpireStatistic(x.getName(), x.expireStatistic()))
                .filter(ResponseExpireStatistic::isExpire)
                .toList();
    }

    @Override
    public void validateName(Name name) {
        if (!repository.containsKey(name)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
        }
    }

    @Override
    public void validateDate() {
        LectureTime.findByLocalDate(today);
        if (HoliDay.contains(today)) {
            String format = String.format("[ERROR] %s은 등교일이 아닙니다.", today.format(FORMATTER));
            throw new IllegalArgumentException(format);
        }
    }

    private void validateTime(LocalTime localTime) {
        if (!CampusTime.isWithin(localTime)) {
            throw new IllegalArgumentException("[ERROR] 출석 시간이 아닙니다");
        }
    }

    private Attendance findByName(Name name) {
        return repository.findByKey(name);
    }
}
