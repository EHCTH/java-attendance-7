package attendance.interfaces.outbound;

import attendance.application.inbound.AttendanceUseCase;
import attendance.domain.Expire;
import attendance.domain.Period;

import java.io.FilterOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OutputView {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 E요일");
    public void displayMenu(LocalDate localDate) {
        System.out.printf("오늘은 %s입니다. 기능을 선택해 주세요.%n1. 출석 확인%n2. 출석 수정%n3. 크루별 출석 기록 확인%n4. 제적 위험자 확인%nQ. 종료%n", localDate.format(formatter));
    }

    public void displayCheckIn(AttendanceUseCase.ResponseBase responseBase) {
        Period period = responseBase.period();
        String output = convertOutput(period);
        System.out.printf("%n%s %s (%s)%n%n",
                output,
                convertTime(period.getLocalTime()),
                period.computeStatus().getDisplay());
    }

    public void displayModify(AttendanceUseCase.ResponseModify modify) {
        AttendanceUseCase.ResponseBase responseBefore = modify.before();
        AttendanceUseCase.ResponseBase responseAfter = modify.after();

        Period before = responseBefore.period();
        Period after = responseAfter.period();

        System.out.printf("%n%s %s (%s) -> %s (%s) 수정 완료!%n%n",
                convertOutput(before),
                convertTime(before.getLocalTime()),
                before.computeStatus().getDisplay(),
                convertTime(after.getLocalTime()),
                after.computeStatus().getDisplay()
        );
    }

    public void displaySearch( AttendanceUseCase.ResponseSearch search) {
        System.out.printf("%n이번 달 %s의 출석 기록입니다.%n%n", search.name().getName());
        search.responseBases()
                .forEach(this::displaySearch);

        System.out.printf("%n출석: %d회%n", search.presentCount());
        System.out.printf("지각: %d회%n", search.lateCount());
        System.out.printf("결석: %d회%n", search.absentCount());

        Expire expire = search.expire();
        if (expire.isExpire()) {
            System.out.printf("%n%s 대상자입니다.%n%n", expire.getDisplay());
        }
    }

    public void displayExpire(List<AttendanceUseCase.ResponseExpire> expire) {
        System.out.printf("%n제적 위험자 조회 결과%n");

        expire.forEach(this::displayExpire);
        System.out.println();
    }

    public void displaySearch(AttendanceUseCase.ResponseBase responseBase) {
        Period period = responseBase.period();
        String output = convertOutput(period);
        System.out.printf("%s %s (%s)%n",
                output,
                convertTime(period.getLocalTime()),
                period.computeStatus().getDisplay());
    }

    private void displayExpire(AttendanceUseCase.ResponseExpire expire) {
        System.out.printf("- %s: 결석 %d회, 지각 %d회 (%s)%n",
                expire.name().getName(),
                expire.absentCount(),
                expire.lateCount(),
                expire.expire().getDisplay()
        );

    }

    private String convertTime(LocalTime localTime) {
        if (localTime == null) {
            return "--:--";
        }
        return localTime.toString();
    }

    private String convertOutput(Period period) {
        LocalDate localDate = period.getLocalDate();
        return localDate.format(formatter);

    }
}
