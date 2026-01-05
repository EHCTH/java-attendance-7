package attendance.domain;

import java.util.Arrays;

public enum MenuOption {
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

    MenuOption(String code) {
        this.code = code;
    }


    public boolean isRetry() {
        return true;
    }

    public static MenuOption findByCode(String code) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 옵션은 존재하지 않습니다"));
    }
}
