package attendance.domain;

import java.util.Comparator;

public record ExpireStatistic(long absent, long late, long present, Expire expire) implements Comparable<ExpireStatistic> {
    private static final Comparator<ExpireStatistic> ORDER_BY_PENALTY = Comparator.comparing(ExpireStatistic::penalty);
    private long penalty() {
        return late * 3 + absent;
    }
    public boolean isExpire() {
        return expire.isExpire();
    }
    @Override
    public int compareTo(ExpireStatistic o) {
        return ORDER_BY_PENALTY.compare(this, o);
    }
}
