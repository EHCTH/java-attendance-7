package attendance.application.port.outbound;

import java.util.List;


public interface Repository<T, R>{
    void save(R value);

    R findByKey(T key);

    boolean containsKey(T key);

    List<R> findAll();
}
