Lv3 -> Lv4 변동사항
1. 게시글 조회 api : /memo/search/~ 로 수정
2. SpringSecurity + Jwt 유효성 검증 필터 추가하여 토큰 검증(기존에는 서비스 단에서 토큰에 대한 검증 작업 진행)
3. 토큰 검증 방식 변경에 따라 컨트롤러 단에서 인증 객체에 담긴 정보를 받아 서비스 단에서 활용
4. 비밀번호 암호화

Lv4 -> Lv5 변동사항
1. 게시글 및 댓글 좋아요 기능 추가
2. AOP 적용해서 부가 기능(api 관련 예외처리) 모듈화(RestApiExceptionHandler)
3. 스프링 시큐리티에서 token == null 일 때 로그인 페이지로 이동(Lv4) => response body에 에러메시지 보내기(Lv5)
