package attendance.application.service;

import java.util.List;

public interface Mapper {
    Row toMap(List<String> data);
    record Row(String name, String dateData) {


    }
}
