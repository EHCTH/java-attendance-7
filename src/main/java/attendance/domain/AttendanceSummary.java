package attendance.domain;

import java.util.Comparator;

public record AttendanceSummary(long absent, long late, long present, Expire expire) {
    public long convertLateToAbsent() {
        return absent * 3 + late;
    }
    public boolean isExpire() {
        return expire.isExpire();
    }


}
