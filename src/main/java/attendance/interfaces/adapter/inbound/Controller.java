package attendance.interfaces.adapter.inbound;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.bootstrap.DataInitializer;
import attendance.domain.LectureTime;
import attendance.domain.MenuOption;
import attendance.domain.Name;
import attendance.interfaces.adapter.outbound.InputView;
import attendance.interfaces.adapter.outbound.OutputView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Controller {
    private final LocalDate today;
    private final InputView inputView;
    private final OutputView outputView;
    private final AttendanceUseCase useCase;
    private final Map<MenuOption, Runnable> options;
    private final DataInitializer dataInitializer;

    public Controller(LocalDate today, InputView inputView, OutputView outputView, AttendanceUseCase useCase, DataInitializer dataInitializer) {
        this.today = today;
        this.inputView = inputView;
        this.outputView = outputView;
        this.useCase = useCase;
        this.dataInitializer = dataInitializer;
        this.options = Map.of(
                MenuOption.CHECK_IN, this::checkIn,
                MenuOption.MODIFY, this::modify,
                MenuOption.SEARCH, this::search,
                MenuOption.EXPIRE, this::expire,
                MenuOption.QUIT, () -> {
                }
        );
    }

    public void run() {
        MenuOption menuOption;
        do {
            menuOption = retryUntilSuccess(() -> inputView.promptMenuOption(today));
            options.get(menuOption).run();
        } while (menuOption.isRetry());
        dataInitializer.fetch();
    }

    private void checkIn() {
        validateWeekend(today);
//        AttendanceUseCase.Response response = retryUntilSuccess(() -> {
//            Name name = inputView.promptCheckInName();
//            useCase.validateExistName(name);
//            LocalTime localTime = inputView.promptCheckInTime();
//            return useCase.checkIn(name, today, localTime);
//        });
        Name name = inputView.promptCheckInName();
        useCase.validateExistName(name);
        LocalTime localTime = inputView.promptCheckInTime();
        AttendanceUseCase.Response response = useCase.checkIn(name, today, localTime);
        outputView.displayCheckIn(response);

    }

    private void modify() {
        Name name = inputView.promptModifyName();
        useCase.validateExistName(name);
        LocalDate modifyDay = inputView.promptModifyDay(today);
        LocalTime modifyTime = inputView.promptModifyTime();

        AttendanceUseCase.ResponseModify responseModify = useCase.modify(name, modifyDay, modifyTime);
        outputView.displayModify(responseModify);
    }

    private void search() {
        Name name = inputView.promptCheckInName();
        useCase.validateExistName(name);
        AttendanceUseCase.ResponseSearch responseSearch = useCase.search(name);
        outputView.displaySearch(responseSearch);
    }

    private void expire() {
        List<AttendanceUseCase.ResponseExpire> expire = useCase.expire();
        outputView.displayExpire(expire);
    }

    private <T> T retryUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateWeekend(LocalDate localDate) {
        LectureTime.findByDayOfWeek(localDate);
    }

}
