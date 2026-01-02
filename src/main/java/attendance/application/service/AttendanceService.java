package attendance.application.service;

import attendance.application.inbound.AttendanceUseCase;
import attendance.application.outbound.AttendanceRepository;
import attendance.domain.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendanceService implements AttendanceUseCase {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MM월 dd일 E요일", Locale.KOREA);
    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseBase checkIn(Name name, LocalDate localDate, LocalTime localTime) {
        Attendance attendance = repository.findByName(name);
        Period checkIn = attendance.checkIn(Period.present(localDate, localTime));
        return createResponseBase(checkIn);
    }

    @Override
    public ResponseModify modify(Name name, int day, LocalTime localTime) {
        Attendance attendance = repository.findByName(name);
        Period before = attendance.findByDay(day);
        Period after = attendance.modify(day, localTime);
        return new ResponseModify(
                createResponseBase(before),
                createResponseBase(after)
        );
    }

    @Override
    public ResponseSearch search(Name name) {
        Attendance attendance = repository.findByName(name);
        List<Period> search = attendance.search();


        List<ResponseBase> responseBases = search.stream()
                .map(this::createResponseBase)
                .toList();

        Map<Status, Long> statusLongMap = attendance.groupingByStatusAndCount();
        Long presentCount = statusLongMap.getOrDefault(Status.PRESENT, 0L);
        Long lateCount = statusLongMap.getOrDefault(Status.LATE, 0L);
        Long absentCount = statusLongMap.getOrDefault(Status.ABSENT, 0L);

        long lateToAbsentCount = covertLateToAbsentCount(lateCount);
        Expire expire = Expire.computeByCount(lateToAbsentCount + absentCount);
        return new ResponseSearch(name, responseBases, presentCount, lateCount, absentCount, expire);
    }

    @Override
    public List<ResponseExpire> expire() {
        return repository.findAll()
                .stream()
                .map(this::createResponseExpire)
                .filter(this::isExpire)
                .sorted()
                .toList();


    }

    @Override
    public void validateExistName(Name name) {
        if (!repository.contains(name)) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 닉네임입니다.");
        }
    }


    @Override
    public void validateDate(LocalDate localDate) {
        if (HoliDay.isHoliday(localDate) || isWeekend(localDate)) {
            String format = localDate.format(formatter);
            String output = String.format("[ERROR] %s은 등교일이 아닙니다.", format);
            throw new IllegalArgumentException(output);
        }
    }

    private long covertLateToAbsentCount(long lateCount) {
        return lateCount / 3;
    }

    private boolean isWeekend(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private ResponseBase createResponseBase(Period period) {
        return new ResponseBase(period, period.computeStatus());
    }

    private ResponseExpire createResponseExpire(Attendance attendance) {
        Name name = attendance.getName();
        Map<Status, Long> statusLongMap = attendance.groupingByStatusAndCount();

        Long lateCount = statusLongMap.getOrDefault(Status.LATE, 0L);
        Long absentCount = statusLongMap.getOrDefault(Status.ABSENT, 0L);
        Long lateToAbsentCount = covertLateToAbsentCount(lateCount);

        Expire expire = Expire.computeByCount(lateToAbsentCount + absentCount);
        return new ResponseExpire(name, absentCount, lateCount, expire);
    }

    private boolean isExpire(ResponseExpire responseExpire) {
        return responseExpire.expire().isExpire();
    }
}
