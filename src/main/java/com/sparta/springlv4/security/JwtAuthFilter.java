package com.sparta.springlv4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springlv4.dto.SecurityExceptionDto;
import com.sparta.springlv4.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            if (!jwtUtil.validationToken(token)) { // 토큰이 유효하지 않을 때
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(HttpStatus.UNAUTHORIZED.value(), "Token Error"));
                response.getWriter().write(json);
                return;
            }
            // 토큰이 유효할 때
            Claims info = jwtUtil.getUserInfoFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(info.getSubject());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        // token == null 인 경우
        // - permitAll() 해 놓은 요청들은 문제 없이 그 다음 필터들도 통과
        // 그렇지 않은 요청들은 인증 객체가 없으므로 다음 필터들에서 걸러짐
        filterChain.doFilter(request, response);
    }
}
