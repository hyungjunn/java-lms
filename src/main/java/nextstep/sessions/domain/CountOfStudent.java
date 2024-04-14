package nextstep.sessions.domain;

public class CountOfStudent {

    private int currentCountOfStudents;

    private final int maxOfStudents;

    private final SessionType sessionType;

    public CountOfStudent(int currentCountOfStudents, int maxOfStudents, SessionType sessionType) {
        if (sessionType.isCapacityExceeded(currentCountOfStudents, maxOfStudents)) {
            throw new IllegalArgumentException(String.format("이 강의의 현재 수강 신청 인원: (%s)명, 최대 수강 인원: (%s)명이므로 현재 마감이 된 상태입니다.", currentCountOfStudents, maxOfStudents));
        }
        this.currentCountOfStudents = currentCountOfStudents;
        this.maxOfStudents = maxOfStudents;
        this.sessionType = sessionType;
    }

    public void increaseCountOfStudents() {
        this.currentCountOfStudents++;
        if (sessionType.isCapacityExceeded(currentCountOfStudents, maxOfStudents)) {
            throw new IllegalArgumentException(String.format("이 강의의 현재 수강 신청 인원: (%s)명, 최대 수강 인원: (%s)명이므로 현재 마감이 된 상태입니다.", currentCountOfStudents, maxOfStudents));
        }
    }

}
