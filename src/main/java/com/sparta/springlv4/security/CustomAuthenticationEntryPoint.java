package com.sparta.springlv4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springlv4.dto.SecurityExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final SecurityExceptionDto exceptionDto = new SecurityExceptionDto(HttpStatus.UNAUTHORIZED.value(), "토큰이 유효하지 않습니다.");

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = new ObjectMapper().writeValueAsString(exceptionDto);
        response.getWriter().write(json);
    }

}
