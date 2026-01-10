package attendance;

import attendance.bootstrap.AppConfig;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        // TODO: 리스트 자르는거 연습및 걔속 복습하자 일어나자마자 템플릿 작성하
        AppConfig appConfig = new AppConfig();
        appConfig.controller().run();;

    }
}
