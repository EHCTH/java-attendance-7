package attendance.application.port.inbound;

import attendance.domain.Expire;
import attendance.domain.ExpireStatistic;
import attendance.domain.Name;
import attendance.domain.VisitDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public interface AttendanceUseCase {
    Response checkIn(Name name, LocalTime localTime);

    ResponseModify modify(Name name, LocalDate localDate, LocalTime localTime);

    ResponseSearch search(Name name);

    List<ResponseExpireStatistic> expire();

    void validateName(Name name);
    void validateDate();

    record Response(VisitDate visitDate) {

    }

    record ResponseModify(Response before, Response after) {

    }

    record ResponseSearch(Name name, List<Response> responseList, ExpireStatistic expireStatistic) {

    }
    record ResponseExpireStatistic(Name name,  ExpireStatistic expireStatistic) implements Comparable<ResponseExpireStatistic> {
        public boolean isExpire() {
            return expireStatistic.isExpire();
        }
        @Override
        public int compareTo(ResponseExpireStatistic o) {
            return Comparator.comparing(ResponseExpireStatistic::expireStatistic, Comparator.reverseOrder())
                    .thenComparing(ResponseExpireStatistic::name)
                    .compare(this, o);
        }
    }
}
