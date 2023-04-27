package com.sparta.springlv4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    @Pattern(regexp = "^[0-9a-z]{4,10}$", message = "4 ~ 10자 사이의 알파벳 소문자와 숫자만 가능합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])[\\da-zA-z!@#$%^&*]{8,15}$", message = "8 ~ 15자 사이의 알파벳 대소문자와 숫자, 특수문자로 구성되어야 합니다.(알파벳 대소문자, 숫자, 특수문자가 최소 1개씩 포함되어야 합니다)")
    private String password;

    private boolean admin;
    private String adminToken = "";
}
