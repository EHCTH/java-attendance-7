package attendance;

import attendance.bootstrap.AppConfig;
import attendance.interfaces.inbound.Controller;

public class Application {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        Controller controller = config.controller();
        controller.run();
    }
}
