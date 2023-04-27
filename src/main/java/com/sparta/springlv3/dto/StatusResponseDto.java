package com.sparta.springlv3.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class StatusResponseDto implements GeneralResponseDto{
    private String msg;
    private HttpStatus status;
}
