package com.sparta.springlv4.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 - BAD REQUEST
    ALREADY_ENROLLED("중복된 username 입니다.", HttpStatus.BAD_REQUEST),
    WRONG_ADMIN_PASSWORD("관리자 암호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    WRONG_USER_ID("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    WRONG_USER_PASSWORD("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),

    // 403 - FORBIDDEN
    CANNOT_MODIFY_OR_DELETE("작성자만 삭제/수정할 수 있습니다.", HttpStatus.FORBIDDEN),

    // 404 - NOT FOUND
    NONEXISTENT_MEMO("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    NONEXISTENT_COMMENT("존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND);

    private String msg;
    private HttpStatus httpStatus;

    ErrorCode(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}
