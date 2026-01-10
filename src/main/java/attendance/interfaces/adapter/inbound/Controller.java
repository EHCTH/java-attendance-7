package attendance.interfaces.adapter.inbound;

import attendance.application.port.inbound.AttendanceUseCase;
import attendance.domain.Name;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Controller {
    private final AttendanceUseCase useCase;
    private final InputView inputView;
    private final OutputView outputView;
    private final Map<MenuOptionType, Runnable> options;

    public Controller(AttendanceUseCase useCase, InputView inputView, OutputView outputView) {
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
            menuOptionType = retryUntilSuccess(inputView::promptMenuOption);
            options.get(menuOptionType).run();
        } while (menuOptionType.isRetry());
    }

    private void checkIn() {
        useCase.validateDate();
        outputView.displayName();
        Name name = inputView.promptName();
        useCase.validateName(name);
        outputView.displayTime();
        LocalTime localTime = inputView.promptLocalTime();
        AttendanceUseCase.Response response = useCase.checkIn(name, localTime);
        outputView.displayChekIn(response);
    }

    private void modify() {
        Name name = inputView.promptName();
        useCase.validateName(name);
        LocalDate localDate = inputView.promptLocalDate();
        LocalTime localTime = inputView.promptLocalTime();
        AttendanceUseCase.ResponseModify modify = useCase.modify(name, localDate, localTime);
        outputView.displayModify(modify);


    }

    private void search() {
        Name name = inputView.promptName();
        useCase.validateName(name);
        AttendanceUseCase.ResponseSearch search = useCase.search(name);
        outputView.displaySearch(search);
    }

    private void expire() {
        List<AttendanceUseCase.ResponseExpireStatistic> expire = useCase.expire();
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
}
