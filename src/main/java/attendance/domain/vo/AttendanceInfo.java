package attendance.domain.vo;

public record AttendanceInfo(long present, long absent, long late, Expire expire) {
}
