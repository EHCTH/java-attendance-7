package attendance.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void computeByDelay() {
        Status absent = Status.computeByDelay(Duration.ofMinutes(31));
        assertThat(absent).isEqualTo(Status.ABSENT);

        Status late = Status.computeByDelay(Duration.ofMinutes(30));
        assertThat(late).isEqualTo(Status.LATE);

        Status present = Status.computeByDelay(Duration.ofMinutes(5));
        assertThat(present).isEqualTo(Status.PRESENT);

        Status present2 = Status.computeByDelay(Duration.ofMinutes(0));
        assertThat(present2).isEqualTo(Status.PRESENT);
    }
}
