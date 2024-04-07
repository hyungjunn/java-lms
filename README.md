# 학습 관리 시스템(Learning Management System)
## 1단계 - 레거시 코드 리팩터링
### 질문 삭제하기 요구사항
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능하다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
### 리팩터링 요구사항
- QnaService의 deleteQuestion()는 앞의 질문 삭제 기능을 구현한 코드이다. 이 메소드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
- QnaService의 deleteQuestion() 메서드에 단위 테스트 가능한 코드(핵심 비지니스 로직)를 도메인 모델 객체에 구현한다.
- QnaService의 비지니스 로직을 도메인 모델로 이동하는 리팩터링을 진행할 때 TDD로 구현한다.
- QnaService의 deleteQuestion() 메서드에 대한 단위 테스트는 src/test/java 폴더 QnaServiceTest이다. 도메인 모델로 로직을 이동한 후에도 QnaServiceTest의 모든 테스트는 통과해야 한다.

### 첫번째 피드백 사항
- [x] 리팩터링할 떼 무조건 복붙하지말고 그 도메인에 알맞는 예외 메시지를 설정하라(Answer, Answers)
- [x] QnaServiceTest는 변경없이 프로덕션코드만 리팩터링하여 재통과하는 사이클을 가져라
- [x] 더 이상 사용하지 않는 메서드는 제거하라
- [x] delete메서드와 addTo메서드의 로직을 다시 살펴보고 구현한 메서드는 무조건 테스트코드를 통해 검증하라
- [x] 사용하지 않는 import문은 삭제하라
- [x] 연관되어있는 메서드는 가깝게 배치시켜라 
- [x] 기능 목록 및 commit 로그 요구 사항을 작성하여 tdd를 의식적으로 연습하라(이후 지켜야할 행동)
- [x] 삭제 이력의 일급 컬렉션 목적성을 생각해보아라 

### 두번째 피드백 사항
- [x] 메서드를 분리하는 건 단일 책임을 위해선 좋지만 연관이 되어있는 메서드들이라면 예외를 적용하라
- [x] Answer#addTo 접근 제한자를 private으로 변경하라

## 2단계 - 수강신청(도메인 모델)
### 수강 신청 기능 요구사항
- 과정(course)은 기수 단위로 운영하며, 여러 개의 강의(Session)를 가질 수 있다.
- 강의는 시작일과 종료일을 가진다.
- 강의는 강의 커버 이미지 정보를 가진다.
  - 이미지 크기는 1MB 이하여야 한다.
  - 이미지 타입은 gif, jpg(jpeg 포함), png, svg만 허용한다.
  - 이미지의 width는 300픽셀, height는 200픽 이상이어야 하며, width와 height의 비율은 3:2이어야 한다.
- 강의는 무료 강의와 유료 강의로 나뉜다.
  - 무료 강의는 최대 수강 인원 제한이 없다.
  - 유료 강의는 강의 최대 수강 인원을 초과할 수 없다.
  - 유료 강의는 수강생이 결제한 금액과 수강료가 일치할 때 수강 신청이 가능하다.
- 강의 상태는 준비중, 모집중, 종료 3가지 상태를 가진다.
- 강의 수강신청은 강의 상태가 모집중일 때만 가능하다.
- 유료 강의의 경우 결제는 이미 완료한 것으로 가정하고 이후 과정을 구현한다.
  - 결제를 완료한 결제 정보는 payments 모듈을 통해 관리되며, 결제 정보는 Payment 객체에 담겨 반한된다.

### 구현 목록 사항(2단계)
- SessionCoverImage
  - [ ] 이미지 크기 > 1MB(1024 * 1024) => 예외 반환
  - [ ] 이미지 타입을 이넘으로 정의하고, 각각 하나씩만 생성하는 것을 검증한다
- SessionCoverImageSize
  - [ ] 이미지 width < 300 => 예외 반환
  - [ ] 이미지 height < 200 => 예외 반환  
  - [ ] 이미지 비율이 3:2 X => 예외 반환
- Session
  - [ ] 시작일과 종료일을 갖는다 
  - [ ] 수강신청을 한다
- SessionStatus
  - [ ] 강의의 상태를 이넘으로 정의한다
  - [ ] 강의의 상태가 모집중일때만 수강신청이 된다
- FreeSession(무료 강의)
  - [ ] 수강 제한이 없다
- PaidSession(유료 강의)
  - [ ] 최대 수강 인원을 초과 => 예외 반환
  - [ ] 수강료 == 결제 금액 => 수강신청 

### 첫번째 피드백 사항
- [x] 누락된 요구사항을 추가하라
- [ ] 무료, 유료 강의의 인원 제한수를 각각 하나로 분리하라(작은 단위의 기능으로 나누어라)
- [ ] 작성한 기능들을 객체 단위로 나누어라 
- [ ] 하나의 항목을 하나의 단위테스트로 DisplayName에 그대로 붙여넣어라 
- [ ] 규칙 3: 3개 이상의 인스턴스 변수를 쓰지 않는다
- [ ] 생성자 위치 컨벤션을 맞추고 각각의 부생성자는 항상 주 생성자를 호출하는 방식으로 생성하라 
- [ ] public 메서드는 테스트코드 대상이다
- [ ] 값과 비교하는 형태가 아닌 메세지를 보내는 형태로 구현하라
- [ ] 변수들의 타입을 왠만하면 일정하게 유지하라 
- [ ] 변수명을 축약하지 말고 풀어서 써라
- [ ] 시작일 종료일을 기간으로 추상화라하
- [ ] 매직넘버를 지양하라
- [ ] 비교하는 로직은 항상 꼼꼼하게 작성하라
- [ ] 변수를 선언할 땐 의미있는 변수만 선언하라
- [ ] 객체의 책임을 많이 가지게 되는 형태로 구성하지 마라 
- [ ] 예외 검증시 테스트하고자 하는 대만 람다식으로 표현하라 
- [ ] 테스트코드도 디테일하게 무엇을 테스트하고자 하는지를 나타내라
- [ ] 반복되는 것은 하나로 만든다
- [ ] (가독성 증가 측면에서)인자를 나타낼 때 변수로 선언한 후 인자를 나타내라

