package attendance.intrefaces.outbound;

import attendance.application.inbound.AttendanceUseCase;
import attendance.domain.Name;
import attendance.domain.vo.Period;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OutputView {
    public void displayCheckIn(AttendanceUseCase.ResponseAttendance responseAttendance) {
        nextLine();
        display(responseAttendance);
        nextLine();
    }

    public void displayModify(List<AttendanceUseCase.ResponseAttendance> responseAttendanceList) {
        AttendanceUseCase.ResponseAttendance before = responseAttendanceList.getFirst();
        AttendanceUseCase.ResponseAttendance after = responseAttendanceList.getLast();

        nextLine();
        displayModify(before, after);
        nextLine();
    }
    public void displaySearch(Name name, AttendanceUseCase.ResponseAttendanceRecords search) {
        System.out.printf("%n이번 달 %s의 출석 기록입니다.%n", name.value());
        nextLine();

        search.records()
                .forEach(this::display);
        nextLine();

        System.out.printf(
                "출석: %d회%n" +
                "지각: %d회%n" +
                "결석: %d회%n",
                search.present(), search.late(), search.absent());
        nextLine();

        if (search.expire().isExpire()) {
            System.out.printf("%s 대상자입니다.%n", search.expire().getDisplay());
            nextLine();
        }

    }

    public void displayExpire(List<AttendanceUseCase.ResponseExpire> expires) {
        System.out.printf("제적 위험자 조회 결과%n");
        expires.forEach(this::displayExpire);
        nextLine();
    }

    private void displayExpire(AttendanceUseCase.ResponseExpire expire) {
        System.out.printf("- %s: 결석 %d회, 지각 %d회 (%s)%n",
                expire.name().value(),
                expire.absent(),
                expire.late(),
                expire.expire().getDisplay()
                );

    }

    private String convertTime(LocalTime localTime) {
        if ( localTime == null) {
            return "--:--";
        }
        return localTime.toString();
    }

    private void display(AttendanceUseCase.ResponseAttendance responseAttendance) {
        Period period = responseAttendance.period();
        LocalDate date = period.getDate();
        System.out.printf("%d월 %02d일 %s %s (%s)%n",
                date.getMonthValue(),
                date.getDayOfMonth(),
                period.korDayOfWeek().getDisplay(),
                convertTime(period.getTime()),
                responseAttendance.status().getDisplay()
        );
    }
    private void displayModify(AttendanceUseCase.ResponseAttendance before, AttendanceUseCase.ResponseAttendance after) {
        Period period = before.period();
        LocalDate date = period.getDate();
        String beforeDisplay = String.format("%d월 %02d일 %s %s (%s)",
                date.getMonthValue(),
                date.getDayOfMonth(),
                period.korDayOfWeek().getDisplay(),
                convertTime(period.getTime()),before.status().getDisplay());

        System.out.printf("%s -> %s (%s) 수정 완료!%n",
                beforeDisplay,
                convertTime(after.period().getTime()),
                after.status().getDisplay());

    }
    private void nextLine() {
        System.out.println();
    }
}
