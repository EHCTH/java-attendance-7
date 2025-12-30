package attendance.intrefaces.inbound;

import java.util.Arrays;

public enum Category {
    CHECK_IN("1"),
    MODIFY("2"),
    SEARCH("3"),
    EXPIRE("4"),
    QUIT("Q") {
        @Override
        public boolean isRetry() {
            return false;
        }
    };
    private final String code;

    Category(String code) {
        this.code = code;
    }

    public boolean isRetry() {
        return true;
    }

    public static Category findByCode(String code) {
        return Arrays.stream(values())
                .filter(category -> category.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 코드는 존재하지 않습니다"));
    }

}
