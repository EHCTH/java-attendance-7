package attendance.application.port.inbound;

import attendance.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public interface AttendanceUseCase {
    record Response(Period period, Status status) {
    }

    record ResponseModify(Response before, Response after) {
    }

    record ResponseSearch(Name name, List<Response> responseList, AttendanceSummary summary) {
    }

    record ResponseExpire(Name name, AttendanceSummary summary) {

        private static final Comparator<ResponseExpire> ORDER =
                Comparator.comparing(ResponseExpire::convertLateToAbsent, Comparator.reverseOrder())
                        .thenComparing(ResponseExpire::name, Comparator.reverseOrder());

        public static Comparator<ResponseExpire> order() {
            return ORDER;
        }

        public boolean isExpire() {
            return summary.isExpire();
        }

        public long convertLateToAbsent() {
            return summary.convertLateToAbsent();
        }
    }

    Response checkIn(Name name, LocalDate localDate, LocalTime localTime);

    ResponseModify modify(Name name, LocalDate modifyDay, LocalTime modifyTime);

    ResponseSearch search(Name name);

    List<ResponseExpire> expire();

    void validateExistName(Name name);

}
