package attendance.interfaces.inbound;

import java.util.Arrays;

public enum Menu {
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

    Menu(String code) {
        this.code = code;
    }
    public static Menu findByCode(String code) {
        return Arrays.stream(values())
                .filter(menu -> menu.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당 옵션은 존재하지 않습니다"));
    }
    public boolean isRetry() {
        return true;
    }
}
