## 1. 기능
- 로그인
- 송금
- 대출 상환

## 2. 분담
- 광열: DB 연동, 계좌 비밀번호 검증, main
- 유진: DB 설계, 로그인, main
- 세은: 송금 전체 로직(Controller, Model)
- 현규: 대출 전체 로직(Controller, Model)

## 3. 트러블슈팅
- 전체 트러블 슈팅
  
  |**문제**|**해결**|
  |:---|:---|
  |(1) 고객 테이블에는 존재하나, 입출금 계좌 테이블에 존재하지 않은 고객 존재|- DwAccount 테이블에서 userId 열을 모두 List에 담아서 출력하게 한 후, 입력받은 userId 가 해당 List에 있는지 확인하여 동작하게 함.|
  |(2) DB 설계 시 id(각 테이블의 pk), userId(로그인 시 사용하는 id)를 명확히 구분하지 않아 쿼리 작성 시 혼동|- user 테이블에서의 고객id = id, DwAccount/LoanAccount 테이블에서의 고객id = userId 로 정리 후 수정|
- 송금 로직 트러블 슈팅
  |**문제**|**해결**|
  |:---|:---|
  |(1) DwAccount 테이블에서 송금하는 사람과 받는 사람의 잔액을 동시에 조회해야 했기 때문에 Controller에서 DwAccount에 접근하는 로직이 복잡해짐| - 서브쿼리를 이용해서 입력받은 id로는 '나의 계좌 잔액'을 SELECT하고, 입력받은 수취인 계좌로는 '수취인의 계좌 잔액'을 SELECT함 <br/> - DTO에서 생성한 update 함수도 송금하는 사람(나)과 받는 사람(수취인)에 따라 다르게 생성|


## 4. 리뷰: KPT(Keep, Problem, Try)
- Keep: 잘 했기 때문에 유지하고 싶은 것
- Problem: 어려움을 느껴 개선하고 싶은 것
- Try: 구체적으로 시도할 내용

|**이름**|**Keep**|**Problem**|**Try**|
|:---:|:---:|:---:|:---:|
|광열|Java와 MySQL 연동, 은행시스템 구현|Java 문법 기초 부족|입출금 기능 추가|
|세은|JDBC 활용 경험, 프로젝트를 통해 MVC를 이해하고 DB와 연동하여 은행 시스템 기능을 구현한 것|(1) 입금/출금/조회, 대출신청, 새로운 계좌 생성 기능의 부재 <br/> (2) 입출금 계좌에서 '잔액부족' 기능 구현 실패|(1) 송금과 대출상환 기능 외 전체 은행 기능 추가 <br/> (2) 계좌의 잔액에 따라 서비스를 실행하는 비즈니스 로직으로 수정|
|현규|내용 10|내용 11|내용 12|
|유진|프로젝트 초기 단계에 DB 및 MVC 구조 설계가 원활하게 이루어져 큰 어려움 없이 작업할 수 있었다.|Model.domain 패키지의 DTO와 Controller가 유사한 작업(DB 상호작용)을 하고 있다.|DB와 상호작용 하는 부분은 Model에 한정하고, Controller는 Model에 접근하여 비즈니스로직을 처리하는 방향으로 확장해야할 듯하다.|
