package com.challang.backend.auth.jwt;

import com.challang.backend.auth.exception.AuthErrorCode;
import com.challang.backend.util.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // HTTP 상태 코드를 설정합니다. (401 Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseResponse<Object> errorResponse = new BaseResponse<>(AuthErrorCode.UNAUTHORIZED_REQUEST);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}