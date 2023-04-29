package com.sparta.springlv4.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    // api 호출 시 발생하는 Exception 처리(토큰 인증과 관련된 exception 은 스프링 시큐리티에서 처리)

    // 회원가입 시 username 과 password 의 구성이 알맞지 않을 때
    // ResponseEntity 를 return 하게 하면 json 으로 변환되면서 개행 처리가 안 됌.
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public String processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField());
            sb.append("은(는) ");
            sb.append(fieldError.getDefaultMessage());
            sb.append("\n");
            sb.append(" 입력된 값 : ");
            sb.append(fieldError.getRejectedValue());
            sb.append("\n");
        }

        return sb.toString();
    }

    @ExceptionHandler(value={CustomException.class})
    public ResponseEntity<Object> handleApiRequestException(CustomException ex) {

        RestApiException restApiException = new RestApiException();
        restApiException.setErrorMessage(ex.getErrorCode().getMsg());
        restApiException.setHttpStatus(ex.getErrorCode().getHttpStatus());

        return new ResponseEntity(restApiException, ex.getErrorCode().getHttpStatus());
    }
}
