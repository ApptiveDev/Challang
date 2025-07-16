package com.challang.backend.global.filter;

import static com.challang.backend.auth.jwt.JwtValidationType.VALID_JWT;

import com.challang.backend.auth.jwt.JwtUtil;
import com.challang.backend.auth.service.CustomUserDetailsService;
import com.challang.backend.global.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


// JWT 토큰을 사용해 인증을 처리하는 필터
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final CustomUserDetailsService userDetailsService;


    private static final List<String> SKIP_PATHS = List.of(
            "/api/auth/email/signup",
            "/api/auth/email/signin",
            "/api/auth/email/send",
            "/api/auth/email/check",
            "/api/auth/kakao/signup",
            "/api/auth/kakao/signin",
            "/api/auth/refresh-token"
    );


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SKIP_PATHS.stream().anyMatch(path::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = jwtUtil.resolveToken(request);

        if (token != null) {
            try {
                if (jwtUtil.validateToken(token) == VALID_JWT) {
                    Long userId = jwtUtil.getId(token);
                    UserDetails userDetails = userDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Expired JWT token\"}");
                return; // 필터 체인 중단
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
                return; // 필터 체인 중단
            }
        }

        filterChain.doFilter(request, response);
    }


}
