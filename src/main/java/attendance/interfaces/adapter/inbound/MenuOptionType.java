package attendance.interfaces.adapter.inbound;

import java.util.Arrays;

public enum MenuOptionType {
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

    MenuOptionType(String code) {
        this.code = code;
    }

    public static MenuOptionType findByCode(String code) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 그런 기능은 존재하지 않습니다"));
    }
    public boolean isRetry() {
        return true;
    }

}
