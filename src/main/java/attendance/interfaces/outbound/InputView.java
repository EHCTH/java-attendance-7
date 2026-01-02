package attendance.interfaces.outbound;

import attendance.domain.Name;
import attendance.interfaces.inbound.Menu;
import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InputView {

    public Menu promptMenu() {
        String code = Console.readLine();
        return Menu.findByCode(code);
    }
    public Name promptName() {
        System.out.printf("%n닉네임을 입력해 주세요.%n");
        return new Name(Console.readLine());
    }


    public LocalTime promptTime() {
        System.out.printf("등교 시간을 입력해 주세요.%n");
        try {
            String data = Console.readLine();
            return LocalTime.parse(data);
        }
        catch (DateTimeException dateTimeException) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }
    public Name promptModifyName() {
        System.out.printf("%n출석을 수정하려는 크루의 닉네임을 입력해 주세요.%n");
        return new Name(Console.readLine());
    }

    public int promptModifyDay() {
        System.out.printf("수정하려는 날짜(일)를 입력해 주세요.%n");
        int data = parseInt();
        try {
            LocalDateTime now = DateTimes.now();
            LocalDate.of(now.getYear(), now.getMonth(), data);
            return data;
        } catch (DateTimeException dateTimeException) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    public LocalTime promptModifyTime() {
        System.out.printf("언제로 변경하겠습니까?%n");
        try {
            String data = Console.readLine();
            return LocalTime.parse(data);
        }
        catch (DateTimeException dateTimeException) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }

    private int parseInt() {
        try {
            String code = Console.readLine();
            return Integer.parseInt(code);

        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("[ERROR] 잘못된 형식을 입력하였습니다.");
        }
    }


}
