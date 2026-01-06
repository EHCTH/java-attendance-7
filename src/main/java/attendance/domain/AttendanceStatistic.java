package attendance.domain;

import java.util.Comparator;

public record AttendanceStatistic(long absent, long late, long present, ExpireType expireType)  implements Comparable<AttendanceStatistic>{
    private static final Comparator<AttendanceStatistic> ORDER_BY_PENALTY =
            Comparator.comparingLong(AttendanceStatistic::penalty);

    private long penalty() {
        return absent * 3 + late;
    }

    public boolean isExpire() {
        return expireType.isExpire();
    }
    @Override
    public int compareTo(AttendanceStatistic o) {
        return ORDER_BY_PENALTY.compare(this, o);
    }
}
