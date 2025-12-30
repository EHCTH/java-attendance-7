package attendance.intrefaces.inbound;

import attendance.application.inbound.AttendanceUseCase;
import attendance.domain.Name;
import attendance.intrefaces.outbound.InputView;
import attendance.intrefaces.outbound.OutputView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AttendanceController {
    private final LocalDate today;
    private final Map<Category, Runnable> option;
    private final AttendanceUseCase useCase;
    private final InputView inputView;
    private final OutputView outputView;


    public AttendanceController(LocalDate today, AttendanceUseCase useCase, InputView inputView, OutputView outputView) {
        this.today = today;
        this.inputView = inputView;
        this.outputView = outputView;
        this.option = Map.of(
                Category.CHECK_IN, this::checkIn,
                Category.MODIFY, this::modify,
                Category.SEARCH, this::search,
                Category.EXPIRE, this::expire,
                Category.QUIT, () -> {
                }
        );
        this.useCase = useCase;
    }

    public void run() {
        Category category;
        do {
            category = retryUntilSuccess(() -> inputView.readCategory(today));
            option.get(category).run();

        } while (category.isRetry());
    }

    private void checkIn() {
        useCase.validateDate();
        Name name = inputView.readName();
        useCase.validateContainsName(name);
        LocalTime time = inputView.readTime();
        AttendanceUseCase.ResponseAttendance responseAttendance = useCase.checkIn(name, time);
        outputView.displayCheckIn(responseAttendance);
    }

    private void modify() {
        Name name = inputView.readModifyName();
        useCase.validateContainsName(name);
        int day = inputView.readModifyDay();
        LocalTime modifyTime = inputView.readModifyTime();
        List<AttendanceUseCase.ResponseAttendance> modify = useCase.modify(name, day, modifyTime);
        outputView.displayModify(modify);
    }

    private void search() {
        Name name = inputView.readName();
        useCase.validateContainsName(name);
        AttendanceUseCase.ResponseAttendanceRecords search = useCase.search(name);
        outputView.displaySearch(name, search);
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
}
