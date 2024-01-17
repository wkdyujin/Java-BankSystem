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
- 송금 시 
- 고객 테이블에는 존재하나, 입출금 계좌 테이블에 존재하지 않은 고객 존재
- DB 설계 시 id(각 테이블의 pk), userId(로그인 시 사용하는 id)를 명확히 구분하지 않아 쿼리 작성 시 혼동
- 

## 4. 리뷰: KPT(Keep, Problem, Try)
- Keep: 잘 했기 때문에 유지하고 싶은 것
- Problem: 어려움을 느껴 개선하고 싶은 것
- Try: 구체적으로 시도할 내용

|**이름**|**Keep**|**Problem**|**Try**|
|:---:|:---:|:---:|:---:|
|광열|Java와 MySQL 연동, 은행시스템 구현|Java 문법 기초 부족|입출금 기능 추가|
|세은|내용 6|내용 7|내용 8|
|현규|내용 10|내용 11|내용 12|
|유진|내용 10|내용 11|내용 12|
