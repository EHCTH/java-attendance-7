package attendance.application.port.inbound;

import attendance.domain.AttendanceStatistic;
import attendance.domain.Name;
import attendance.domain.Period;
import attendance.domain.StatusType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public interface AttendanceUseCase {
    Response checkIn(Name name, LocalDate today, LocalTime localTime);

    ResponseModify modify(Name name, LocalDate localDate, LocalTime modifyTime);

    ResponseSearch search(Name name);

    List<ResponseExpire> expire();

    void validateIncludeName(Name name);

    record Response(Period period, StatusType statusType) {

    }

    record ResponseModify(Response before, Response after) {

    }

    record ResponseSearch(Name name, List<Response> responses, AttendanceStatistic statistic) {


    }

    record ResponseExpire(Name name, AttendanceStatistic statistic) implements Comparable<ResponseExpire>{
        public boolean isExpire() {
            return statistic.isExpire();
        }
        @Override
        public int compareTo(ResponseExpire o) {
            return Comparator.comparing(ResponseExpire::statistic, Comparator.reverseOrder())
                    .thenComparing(ResponseExpire::name, Comparator.reverseOrder())
                    .compare(this, o);
        }
    }
}
