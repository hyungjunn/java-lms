package nextstep.sessions.domain;

import nextstep.payments.domain.Payment;
import nextstep.sessions.domain.image.Image;
import nextstep.users.domain.NsUser;
import nextstep.utils.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class Session extends BaseEntity {

    private String sessionName;

    private Image image;

    private SessionDetails sessionDetails;

    private List<NsUser> listeners;

    public Session(long id, String sessionName, SessionDetails sessionDetails, List<NsUser> listeners) {
        this(id, LocalDateTime.now(), LocalDateTime.now(), sessionName, null, sessionDetails, listeners);
    }

    public Session(long id,
                   LocalDateTime startedAt,
                   LocalDateTime endedAt,
                   String sessionName,
                   Image image,
                   SessionDetails sessionDetails,
                   List<NsUser> listeners
    ) {
        super(id, startedAt, endedAt);
        this.sessionName = sessionName;
        this.image = image;
        this.sessionDetails = sessionDetails;
        this.listeners = listeners;
    }

    public void register(NsUser listener, Long amount) {
        if (sessionDetails.isNotSamePrice(amount)) {
            throw new IllegalArgumentException("결제한 금액이 강의의 가격과 일치하지 않습니다.");
        }
        listeners.add(listener);
    }

}
