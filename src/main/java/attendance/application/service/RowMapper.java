package attendance.application.service;

import attendance.domain.Attendance;

import java.util.List;

public class RowMapper implements Mapper {
    private static final int NAME_INDEX = 0;
    private static final int DATE_INDEX = 1;

    @Override
    public Row toMap(List<String> data) {
        String name = data.get(NAME_INDEX);
        String date = data.get(DATE_INDEX);
        return new Mapper.Row(name, date);
    }
}
