package attendance.infrastrucutre;

import attendance.application.port.outbound.AttendanceRepository;
import attendance.domain.Attendance;
import attendance.domain.Name;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryAttendanceRepository implements AttendanceRepository {
    private final Map<Name, Attendance> store = new HashMap<>();
    @Override
    public void save(Attendance attendance) {
        store.put(attendance.getName(), attendance);
    }

    @Override
    public Attendance findByName(Name name) {
        return store.get(name);
    }

    @Override
    public boolean containsKey(Name name) {
        return store.containsKey(name);
    }

    @Override
    public List<Attendance> findAll() {
        return List.copyOf(store.values());
    }

}
