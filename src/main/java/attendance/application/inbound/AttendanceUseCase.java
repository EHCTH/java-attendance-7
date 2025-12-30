package attendance.application.inbound;

import attendance.domain.Name;
import attendance.domain.vo.Expire;
import attendance.domain.vo.Period;
import attendance.domain.vo.Status;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public interface AttendanceUseCase {
    ResponseAttendance checkIn(Name name, LocalTime localTime);

    List<ResponseAttendance> modify(Name name, int day, LocalTime localTime);

    ResponseAttendanceRecords search(Name name);

    List<ResponseExpire> expire();
    void validateContainsName(Name name);
    void validateDate();
    record ResponseAttendance(Name name, Period period, Status status) implements Comparable<ResponseAttendance> {

        @Override
        public int compareTo(ResponseAttendance o) {
            return period.getDate().compareTo(o.period.getDate());
        }
    }

    record ResponseAttendanceRecords(List<ResponseAttendance> records, long present, long late, long absent,
                                     Expire expire) {

    }
    record ResponseExpire(Name name, long absent, long late, Expire expire) implements Comparable<ResponseExpire> {

        public boolean isExpire() {
            return expire.isExpire();
        }

        @Override
        public int compareTo(ResponseExpire o) {
            Comparator<ResponseExpire> comparingByCount = Comparator.comparingLong(ResponseExpire::sortedCount);
            Comparator<ResponseExpire> comparingByName = Comparator.comparing(ResponseExpire::name);

            return Comparator.comparing(ResponseExpire::expire)
                    .thenComparing(comparingByCount.reversed())
                    .thenComparing(comparingByName.reversed())
                    .compare(this, o);
        }

        private long sortedCount() {
            return absent + (late / 3) + (late % 3);
            /*

            정렬 순서는 지각을 결석으로 간주하여 내림차순한다
            위 와 아래는 같은 코드이다

             */
//            return absent * 3 + late;
        }
    }
}
