package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.sessions.domain.RecruitmentStatus.*;
import static nextstep.sessions.domain.RecruitmentStatus.RECRUITING;
import static nextstep.sessions.domain.SessionStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SessionRegisterDetailsTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment("javajigi", 1L, 1L, 30000L);
    }

    @DisplayName("수강신청을 한다")
    @Test
    void always() {
        SessionRegisterDetails details = new SessionRegisterDetails(1L, new Price(30000), IN_PROGRESS, RECRUITING, 40);

        List<Student> students = new ArrayList<>();

        Student student = details.enroll(new Student(1L, 1L), students, payment);

        assertThat(student).isEqualTo(new Student(1L, 1L));
    }

    @DisplayName("강의가 진행중이 아닐때 수강신청을 하면 예외를 반환한다")
    @Test
    void statusIsNotInProgress() {
        SessionStatus end = END;
        SessionRegisterDetails details = new SessionRegisterDetails(1L, new Price(30000), end, 40);

        assertThatThrownBy(() -> details.enroll(new Student(1L, 1L), new ArrayList<>(), payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("현재 강의는 (%s)인 상태입니다.", end));
    }

    @DisplayName("강의가 모집중이 아닐때 수강신청을 하면 예외를 반환한다")
    @Test
    void statusIsNotRecruiting() {
        RecruitmentStatus notRecruiting = NOT_RECRUITING;
        SessionRegisterDetails details = new SessionRegisterDetails(1L, new Price(30000), IN_PROGRESS, notRecruiting, 40);

        assertThatThrownBy(() -> details.enroll(new Student(1L, 1L), new ArrayList<>(), payment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("현재 강의는 (%s)인 상태입니다.", notRecruiting));
    }

    @DisplayName("강의의 가격과 결제한 금액이 같지 않은지 검증한다")
    @Test
    void isSameAmount() {
        SessionRegisterDetails details = new SessionRegisterDetails(1L, new Price(30000),IN_PROGRESS, RECRUITING, 40);

        assertThat(details.isNotSamePrice(20000)).isTrue();
    }
}
