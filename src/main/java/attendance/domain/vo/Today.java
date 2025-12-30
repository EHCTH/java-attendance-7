package attendance.domain.vo;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;

public class Today {
    private final LocalDate today;

    public Today(LocalDate today) {
        this.today = today;
    }

    public static void main(String[] args) {

        LocalDate today = DateTimes.now().toLocalDate();
    }
}
