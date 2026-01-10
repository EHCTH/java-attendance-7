package attendance.interfaces.adapter.inbound;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.domain.ExpireStatistic;
import attendance.domain.VisitDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OutputView {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 EEEE");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public void displayName() {
        System.out.println("닉네임을 입력해 주세요.");
    }
    public void displayTime() {
        System.out.println("등교 시간을 입력해 주세요.");
    }
    public void displayChekIn(AttendanceUseCase.Response response) {
        VisitDate visitDate = response.visitDate();
        String format = formatResponse(visitDate);
        System.out.println(format);
    }

    private String formatResponse(VisitDate visitDate) {
        return String.format("%s %s (%s) %n",
                visitDate.getDate().format(DATE_FORMATTER),
                convertTime(visitDate.getTime()),
                visitDate.computeByDelay().getDisplay()
        );
    }
    
    public void displayModify(AttendanceUseCase.ResponseModify modify) {
        AttendanceUseCase.Response before = modify.before();
        VisitDate beforeVisitDate = before.visitDate();

        AttendanceUseCase.Response after = modify.after();
        VisitDate afterVisitDate = after.visitDate();


        String format = String.format("%s %s (%s) -> %s (%s) 수정 완료!%n",
                beforeVisitDate.getDate().format(DATE_FORMATTER),
                convertTime(beforeVisitDate.getTime()),
                beforeVisitDate.computeByDelay().getDisplay(),
                convertTime(afterVisitDate.getTime()),
                afterVisitDate.computeByDelay().getDisplay());
        System.out.println(format);

    }
    public void displaySearch(AttendanceUseCase.ResponseSearch responseSearch) {
        System.out.printf("이번 달 %s의 출석 기록입니다.%n%n", responseSearch.name().getName());
        responseSearch.responseList()
                .stream()
                .map(AttendanceUseCase.Response::visitDate)
                .map(this::formatResponse)
                .forEach(System.out::println);
        System.out.println();


        ExpireStatistic expireStatistic = responseSearch.expireStatistic();
        System.out.printf("출석: %d회%n", expireStatistic.present());
        System.out.printf("지각: %d회%n", expireStatistic.late());
        System.out.printf("결석: %d회%n%n", expireStatistic.absent());

        System.out.printf("%s 대상자입니다.%n", expireStatistic.expire().getDisplay());
    }

    public void displayExpire(List<AttendanceUseCase.ResponseExpireStatistic> expireStatisticList) {
        System.out.println("제적 위험자 조회 결과");
        expireStatisticList.forEach(this::displayExpire);
    }
    private void displayExpire(AttendanceUseCase.ResponseExpireStatistic expireStatistic) {
        ExpireStatistic statistic = expireStatistic.expireStatistic();
        System.out.printf("%s: 결석 %d회, 지각 %d회 (%s)%n",
                expireStatistic.name().getName(),
                statistic.absent(),
                statistic.late(),
                statistic.expire().getDisplay()         );
    }

    private String convertTime(LocalTime localTime) {
        if (localTime == null) {
            return "--:--";
        }
        return localTime.format(TIME_FORMATTER);
    }
}
