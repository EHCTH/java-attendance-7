package attendance.interfaces.adapter.inbound;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.domain.LectureTime;
import attendance.domain.Name;
import attendance.domain.SpecialDay;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Controller {
    private static final DateTimeFormatter OUTPUT =
            DateTimeFormatter.ofPattern("yyyy월 dd일 EEEE");
    private final LocalDate today;
    private final Map<MenuOptionType, Runnable> options;
    private final AttendanceUseCase useCase;
    private final InputView inputView;
    private final OutputView outputView;


    public Controller(LocalDate today, AttendanceUseCase useCase, InputView inputView, OutputView outputView) {
        this.today = today;
        this.useCase = useCase;
        this.inputView = inputView;
        this.outputView = outputView;
        this.options = Map.of(
                MenuOptionType.CHECK_IN, this::checkIn,
                MenuOptionType.MODIFY, this::modify,
                MenuOptionType.SEARCH, this::search,
                MenuOptionType.EXPIRE, this::expire,
                MenuOptionType.QUIT, () -> {
                }
        );
    }

    public void run() {
        MenuOptionType menuOptionType;
        do {
            menuOptionType = getRetryUntilSuccess(() -> inputView.promptMenuOptionType(today));
            options.get(menuOptionType).run();
        } while (menuOptionType.isRetry());
    }

    private void checkIn() {
        validateDate();
        Name name = inputView.promptCheckInName();
        useCase.validateIncludeName(name);
        LocalTime localTime = inputView.promptCheckInTime();
        AttendanceUseCase.Response response = useCase.checkIn(name, today, localTime);
        outputView.displayCheckIn(response);

    }

    private void modify() {
        Name name = inputView.promptModifyName();
        useCase.validateIncludeName(name);
        LocalDate modifyDay = inputView.promptModifyDay(today);
        LocalTime modifyTime = inputView.promptModifyTime();
        AttendanceUseCase.ResponseModify responseModify = useCase.modify(name, modifyDay, modifyTime);
        outputView.displayModify(responseModify);
    }

    private void search() {
        Name name = inputView.promptCheckInName();
        useCase.validateIncludeName(name);
        AttendanceUseCase.ResponseSearch responseSearch = useCase.search(name);
        outputView.displaySearch(responseSearch);
    }

    private void expire() {
        List<AttendanceUseCase.ResponseExpire> expire = useCase.expire();
        outputView.displayExpire(expire);
    }

    private <T> T getRetryUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void runRetryUntilSuccess(Runnable task) {
        while (true) {
            try {
                task.run();
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void validateDate() {
        LectureTime.findByDate(today);
        if (SpecialDay.isSpecial(today)) {
            throw new IllegalArgumentException(messageConverter(today));
        }

    }
    private static String messageConverter(LocalDate localDate) {
        return "[ERROR]" + localDate.format(OUTPUT) + "은 등교일이 아닙니다.";
    }


}
