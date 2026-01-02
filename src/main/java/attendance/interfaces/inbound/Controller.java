package attendance.interfaces.inbound;

import attendance.application.inbound.AttendanceUseCase;
import attendance.domain.Name;
import attendance.interfaces.outbound.InputView;
import attendance.interfaces.outbound.OutputView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Controller {
    private final LocalDate today;
    private final AttendanceUseCase useCase;
    private final Map<Menu, Runnable> menuRunnableMap;
    private final InputView inputView;
    private final OutputView outputView;

    public Controller(LocalDate today, AttendanceUseCase useCase, InputView inputView, OutputView outputView) {
        this.today = today;
        this.useCase = useCase;
        this.outputView = outputView;
        this.menuRunnableMap = Map.of(
                Menu.CHECK_IN, this::checkIn,
                Menu.MODIFY, this::modify,
                Menu.SEARCH, this::search,
                Menu.EXPIRE, this::expire,
                Menu.QUIT, () -> {}
        );
        this.inputView = inputView;
    }
    public void run() {
        Menu menu;
        do {
            outputView.displayMenu(today);
            menu = retryOrElseThrow(inputView::promptMenu);
            menuRunnableMap.get(menu).run();
        }
        while (menu.isRetry());

    }

    private void checkIn() {
        useCase.validateDate(today);

        Name name = inputView.promptName();
        useCase.validateExistName(name);

        LocalTime localTime = inputView.promptTime();
        AttendanceUseCase.ResponseBase responseBase = useCase.checkIn(name, today, localTime);
        outputView.displayCheckIn(responseBase);

    }
    private void modify() {
        Name name = inputView.promptModifyName();
        useCase.validateExistName(name);
        int day = inputView.promptModifyDay();
        LocalTime modifyTime = inputView.promptModifyTime();

        AttendanceUseCase.ResponseModify modify = useCase.modify(name, day, modifyTime);
        outputView.displayModify(modify);


    }
    private void search() {
        Name name = inputView.promptName();
        useCase.validateExistName(name);

        AttendanceUseCase.ResponseSearch search = useCase.search(name);
        outputView.displaySearch(search);
    }
    private void expire() {
        List<AttendanceUseCase.ResponseExpire> expire = useCase.expire();
        outputView.displayExpire(expire);

    }

    private <T> T retryOrElseThrow(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            }catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
