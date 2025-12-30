package attendance.intrefaces.outbound;

import attendance.domain.Name;
import attendance.domain.vo.KorDayOfWeek;
import attendance.intrefaces.inbound.Category;
import camp.nextstep.edu.missionutils.Console;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InputView {

    public Name readName() {
        System.out.printf("%n닉네임을 입력해 주세요.%n");
        return new Name(readLine());
    }

    public Name readModifyName() {
        System.out.printf("%n출석을 수정하려는 크루의 닉네임을 입력해 주세요.%n");
        return new Name(readLine());
    }
    public LocalTime readTime() {
        System.out.printf("등교 시간을 입력해 주세요.%n");
        return timeOrThrow();
    }

    public int readModifyDay() {
        System.out.printf("수정하려는 날짜(일)를 입력해 주세요.%n");
        String input = Console.readLine();
        try {

            int day = Integer.parseInt(input);
            if (day < 1 || 31 < day) {
                throw new IllegalArgumentException("[ERROR] 1 ~ 31 숫자를 입력해주세요");
            }
            return day;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 숫자를 입력해 주세요.");
        }
    }

    public LocalTime readModifyTime() {
        System.out.printf("언제로 변경하겠습니까?%n");
        return timeOrThrow();
    }

    public Category readCategory(LocalDate today) {
        System.out.printf("오늘은 %d월 %02d일 %s입니다. 기능을 선택해 주세요.%n" +
                "1. 출석 확인%n" +
                "2. 출석 수정%n" +
                "3. 크루별 출석 기록 확인%n" +
                "4. 제적 위험자 확인%n" +
                "Q. 종료%n",
                today.getMonthValue(),
                today.getDayOfMonth(),
                KorDayOfWeek.findByDayOfWeek(today.getDayOfWeek()).getDisplay());
        String category = readLine();
        return Category.findByCode(category);

    }

    private String readLine() {
        return Console.readLine();
    }
    private LocalTime timeOrThrow() {
        try {
            String time = readLine();
            return LocalTime.parse(time);

        } catch (DateTimeParseException dateTimeParseException) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

}
