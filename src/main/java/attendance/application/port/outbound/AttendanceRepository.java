package attendance.application.port.outbound;

import attendance.domain.Attendance;
import attendance.domain.Name;

import java.util.List;

public interface AttendanceRepository {
    void save(Attendance attendance);

    Attendance findByName(Name name);

    List<Attendance> findAll();

    boolean containsKey(Name name);
}
