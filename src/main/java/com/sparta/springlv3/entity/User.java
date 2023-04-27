package com.sparta.springlv3.entity;

import com.sparta.springlv3.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    public User(UserRequestDto userRequestDto, UserRoleEnum role) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
        this.role = role;
    }
}
