package attendance.interfaces.adapter.outbound;

import attendance.domain.MenuOption;
import attendance.domain.Name;
import camp.nextstep.edu.missionutils.Console;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InputView {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MM월 dd일 EEEE");

    public MenuOption promptMenuOption(LocalDate today) {
        System.out.println("오늘은 " + today.format(DATE_TIME_FORMATTER) + "입니다. 기능을 선택해 주세요.\n" +
                "1. 출석 확인\n" +
                "2. 출석 수정\n" +
                "3. 크루별 출석 기록 확인\n" +
                "4. 제적 위험자 확인\n" +
                "Q. 종료");
        return MenuOption.findByCode(readLine());
    }

    public Name promptCheckInName() {
        System.out.printf("%n닉네임을 입력해 주세요.%n");
        return readName();
    }

    public LocalTime promptCheckInTime() {
        System.out.println("등교 시간을 입력해 주세요.");
        return readLocalTime();
    }

    public Name promptModifyName() {
        System.out.printf("%n출석을 수정하려는 크루의 닉네임을 입력해 주세요.%n");
        return readName();
    }

    public LocalDate promptModifyDay(LocalDate today) {
        System.out.printf("수정하려는 날짜(일)를 입력해 주세요.%n");
        return readDay(today);
    }

    public LocalTime promptModifyTime() {
        System.out.printf("언제로 변경하겠습니까?%n");
        return readLocalTime();
    }


    private Name readName() {
        return new Name(readLine());
    }

    private LocalTime readLocalTime() {
        try {
            return LocalTime.parse(readLine());
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    private LocalDate readDay(LocalDate today) {
        try {
            int day = nextInt();
            return today.withDayOfMonth(day);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }

    }

    private int nextInt() {
        try {
            return Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    private String readLine() {
        return Console.readLine();
    }
}
