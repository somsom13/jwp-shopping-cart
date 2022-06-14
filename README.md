# 장바구니

장바구니 미션 저장소

## 1단계 기능 구현 목록

- [x] 회원가입 기능
    - 회원 정보
  ```
  이메일
  패스워드
  유저 닉네임
  ```
    - [x] 이메일이 중복되면 안된다.
        - [x] 패스워드 규칙이 맞아야 한다.
        - 추후에 프론트와 다시 협의
    - [x] 각각의 값이 null이거나 empty면 안된다.
- [x] 로그인 기능
    - 로그인 성공시 토큰을 발급한다.
    - 이메일 또는 패스워드가 일치하지 않으면 예외가 발생한다.
- [x] 회원 정보 조회 기능
    - [x] 자신의 회원 정보만 조회할 수 있다.
    - [x] 토큰이 만료 또는 일치하지 않을 시에 예외를 발생시킨다.
- [x] 회원 수정 기능
    - [x] 비밀 번호 수정 기능
        - [x] 기존 비밀번호가 일치하지 않으면 예외를 발생시킨다.
        - [x] 새로운 비밀번호가 패스워드 규칙에 맞지 않으면 예외를 발생시킨다.
    - [x] 일반 회원 정보 수정 기능
        - 유저 닉네임을 수정할 수 있다.
- [x] 회원 탈퇴 기능
    - [x] 비밀번호가 일치하지 않으면 예외를 발생시킨다.

## 2단계 기능 구현 목록
### 상품 관련 기능
- [x] 상품 목록을 조회할 수 있다.
- [x] 상품을 추가할 수 있다.
- [x] 특정한 상품을 단건으로 조회할 수 있다.
  - [x] 해당 상품이 없으면 예외를 발생시킨다.
- [x] 상품을 삭제할 수 있다.
  - [x] 해당 상품이 없으면 예외를 발생시킨다.
### 장바구니 관련 기능
- [x] 장바구니 목록을 조회할 수 있다.
- [x] 장바구니에 상품을 담을 수 있다.
    - [x] 해당 상품이 존재하지 않으면 예외를 발생시킨다.
- [x] 담아둔 장바구니에서 개수를 수정할 수 있다.
    - [x] 상품의 재고보다 많은 수, 혹은 0 이하의 수로 수정하려고 할 시 예외를 발생시킨다.
- [x] 담아둔 장바구니 내용을 삭제할 수 있다.
### 주문 관련 기능
- [x] 주문 목록을 조회할 수 있다.
- [x] 주문을 추가할 수 있다.
  - [x] 해당 장바구니 상품이 존재하지 않으면 예외를 발생시킨다.
- [x] 주문 목록 내에서 하나의 주문을 단건으로 조회할 수 있다.