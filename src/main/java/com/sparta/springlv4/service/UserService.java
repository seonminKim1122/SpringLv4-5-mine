package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.LoginRequestDto;
import com.sparta.springlv4.dto.StatusResponseDto;
import com.sparta.springlv4.dto.SignupRequestDto;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.entity.UserRoleEnum;
import com.sparta.springlv4.exception.CustomException;
import com.sparta.springlv4.exception.ErrorCode;
import com.sparta.springlv4.repository.UserRepository;
import com.sparta.springlv4.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StatusResponseDto signup(SignupRequestDto signupRequestDto) {
        Optional<User> found = userRepository.findById(signupRequestDto.getUsername());

        if (found.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_ENROLLED);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(ErrorCode.WRONG_ADMIN_PASSWORD);
            }
            role = UserRoleEnum.ADMIN;
        }

        signupRequestDto.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        User user = new User(signupRequestDto, role);
        userRepository.save(user);
        return new StatusResponseDto("회원가입 성공!!", HttpStatus.OK);
    }

    public StatusResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findById(loginRequestDto.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.WRONG_USER_ID)
        );

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_USER_PASSWORD);
        } else {
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
            return new StatusResponseDto("로그인 성공!!", HttpStatus.OK);
        }
    }
}
