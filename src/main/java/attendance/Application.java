package attendance;

import attendance.bootstrap.AppConfig;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        AppConfig appConfig = new AppConfig();
        appConfig.controller().run();
    }
}
/*
mkdir -p src/main/java/attendance/application/port/inbound \
         src/main/java/attendance/application/port/outbound \
         src/main/java/attendance/application/service \
         src/main/java/attendance/infrastructure \
         src/main/java/attendance/domain \
         src/main/java/attendance/bootstrap \
         src/main/java/attendance/interfaces/adapter/inbound
 */
