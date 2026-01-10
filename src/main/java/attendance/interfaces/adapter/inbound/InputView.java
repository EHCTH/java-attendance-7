package attendance.interfaces.adapter.inbound;

import attendance.domain.Name;
import camp.nextstep.edu.missionutils.Console;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InputView {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 EEEE");
    private final LocalDate today;

    public InputView(LocalDate today) {
        this.today = today;
    }

    private String convertMessage() {
        return String.format("오늘은 %s입니다. 기능을 선택해 주세요.", today.format(FORMATTER));
    }

    public MenuOptionType promptMenuOption() {
        System.out.println(convertMessage());
        System.out.println("1. 출석 확인\n" +
                "2. 출석 수정\n" +
                "3. 크루별 출석 기록 확인\n" +
                "4. 제적 위험자 확인\n" +
                "Q. 종료");
        return MenuOptionType.findByCode(readLine());
    }

    public Name promptName() {
        return new Name(readLine());
    }
    public int nextInt() {
        try {
            return Integer.parseInt(readLine());
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 정수를 입력해주세요");
        }
    }

    public LocalDate promptLocalDate() {
        try {
            return today.withDayOfMonth(nextInt());
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }
    public LocalTime promptLocalTime() {
        try {
            return LocalTime.parse(readLine());
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    private String readLine() {
        return Console.readLine();
    }

}
