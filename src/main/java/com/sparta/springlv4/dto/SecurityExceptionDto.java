package com.sparta.springlv4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecurityExceptionDto {
    private int statusCode;
    private String msg;
}
