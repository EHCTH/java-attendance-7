package attendance.application.inbound;

import attendance.domain.Expire;
import attendance.domain.Name;
import attendance.domain.Period;
import attendance.domain.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public interface AttendanceUseCase {
    record ResponseBase(Period period, Status status) {}
    record ResponseModify(ResponseBase before, ResponseBase after) {}

    record ResponseSearch(Name name,
                          List<ResponseBase> responseBases,
                          Long presentCount, Long lateCount, Long absentCount, Expire expire) {

    }

    record ResponseExpire(Name name, Long absentCount, Long lateCount, Expire expire) implements Comparable<ResponseExpire>{

        @Override
        public int compareTo(ResponseExpire o) {
            return Comparator.comparing(ResponseExpire::expire)
                    .thenComparing(ResponseExpire::conversionLateToAbsent, Comparator.reverseOrder())
                    .thenComparing(ResponseExpire::name, Comparator.reverseOrder())
                    .compare(this, o);
        }

        private long conversionLateToAbsent() {
            return absentCount * 3 + lateCount;
        }

    }

    ResponseBase checkIn(Name name, LocalDate localDate, LocalTime localTime);

    ResponseModify modify(Name name, int day, LocalTime localTime);
    ResponseSearch search(Name name);

    List<ResponseExpire> expire();

    void validateExistName(Name name);

    void validateDate(LocalDate localDate);

}
