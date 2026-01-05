package attendance;

import attendance.bootstrap.AppConfig;
import attendance.interfaces.adapter.inbound.Controller;

import java.util.Arrays;
import java.util.List;

public class Application {
        /*
            TODO
                1. Stream, generate, Collectors 공부
                2. InputStream, 즉 resources 파일 리더 공부, 파일 라이터도 공부하자
                3. LocalDateTime, LocalDate, LocalTime, Duration 공부
                4. Regex 정규식 공부
                5. 위 1 ~ 4 반복, 메모장에 이미 요약했음 요약본 필히 복습하자

         */


    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        Controller controller = appConfig.controller();
        controller.run();
    }


    private void test() {
        String s = ",,,,,s,s,s,s,s,,,,";

        List<String> split = Arrays.stream(s.split(","))
//                .filter(x -> !x.isEmpty())
//                .map(String::trim)
//                .map(x -> x.replace("", "b"))
                .toList();

        String hello = "21312";
        System.out.println(split); // [, , , , , s, s, s, s, s]

        List<String> split2 = Arrays.stream(s.split(",", -1))
                .toList();

        System.out.println(split2); // [, , , , , s, s, s, s, s, , , , ]
    }
}
