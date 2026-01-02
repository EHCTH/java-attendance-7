package attendance.domain;

import java.util.Arrays;

public enum Expire {
    EXPIRE(6L, "제적"),
    FACE_TO_FACE(3L, "면담"),
    WARNING(2L,"경고"),
    SAFE(0L, "생존"),;
    private final Long threshold;
    private final String display;

    Expire(Long threshold, String display) {
        this.threshold = threshold;
        this.display = display;
    }

    public static Expire computeByCount(Long count) {
        return Arrays.stream(values())
                .filter(x -> x.threshold <= count)
                .findFirst()
                .orElse(SAFE);
    }
    public Long getThreshold() {
        return threshold;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isExpire() {
        return this != SAFE;
    }

    /*
    지각 3회는 결석 1회로 간주한다.
경고 대상자: 결석 2회 이상
면담 대상자: 결석 3회 이상
제적 대상자: 결석 5회 초과

     */
}
