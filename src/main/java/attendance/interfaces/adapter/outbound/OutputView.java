package attendance.interfaces.adapter.outbound;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OutputView {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 EEEE");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public void displayCheckIn(AttendanceUseCase.Response response) {
        printDate(response);
    }

    public void displayModify(AttendanceUseCase.ResponseModify responseModify) {
        printDate(responseModify.before(), responseModify.after());
    }
    public void displaySearch(AttendanceUseCase.ResponseSearch responseSearch) {
        System.out.printf("%n이번 달 %s의 출석 기록입니다.%n%n",responseSearch.name().getName());

        List<AttendanceUseCase.Response> responses = responseSearch.responseList();
        responses.forEach(this::printDateForEach);

        AttendanceSummary summary = responseSearch.summary();

        System.out.printf("%n출석: %d회%n", summary.present());
        System.out.printf("지각: %d회%n", summary.late());
        System.out.printf("결석: %d회%n", summary.absent());
        Expire expire = summary.expire();
        if (expire.isExpire()) {
            System.out.printf("%n%s 대상자입니다%n%n", summary.expire().getDisplay());
        }
    }

    public void displayExpire(List<AttendanceUseCase.ResponseExpire> expires) {
        System.out.printf("%n제적 위험자 조회 결과%n");
        expires.forEach(this::printExpire);
        System.out.println();
    }

    private void printExpire(AttendanceUseCase.ResponseExpire responseExpire) {
        Name name = responseExpire.name();
        AttendanceSummary summary = responseExpire.summary();
        System.out.printf("- %s: 결석 %d회, 지각 %d회 (%s)%n", name.getName(), summary.absent(), summary.late(), summary.expire().getDisplay());
    }

    private void printDateForEach(AttendanceUseCase.Response response) {
        Period period = response.period();
        LocalDate localDate = period.getLocalDate();
        LocalTime localTime = period.getLocalTime();
        System.out.printf("%s %s %s%n",
                localDate.format(DATE_FORMATTER),
                convertTime(localTime),
                convertStatus(response.status())
        );
    }
    private void printDate(AttendanceUseCase.Response response) {
        Period period = response.period();
        LocalDate localDate = period.getLocalDate();
        LocalTime localTime = period.getLocalTime();
        System.out.printf("%n%s %s %s%n%n",
                localDate.format(DATE_FORMATTER),
                convertTime(localTime),
                convertStatus(response.status())
        );
    }

    private void printDate(AttendanceUseCase.Response before, AttendanceUseCase.Response after) {
        Period beforePeriod = before.period();
        LocalDate beforeDate = beforePeriod.getLocalDate();
        LocalTime beforeTime = beforePeriod.getLocalTime();

        Period afterPeriod = after.period();
        LocalTime afterTime = afterPeriod.getLocalTime();

        System.out.printf("%n%s %s %s -> %s %s 수정 완료!%n%n",
                beforeDate.format(DATE_FORMATTER),
                convertTime(beforeTime),
                convertStatus(before.status()),
                convertTime(afterTime),
                convertStatus(after.status())
        );
    }


    private String convertTime(LocalTime localTime) {
        if (localTime == null) {
            return "--:--";
        }
        return localTime.format(TIME_FORMATTER);
    }

    private String convertStatus(Status status) {
        return String.format("(%s)", status.getDisplay());
    }
}
