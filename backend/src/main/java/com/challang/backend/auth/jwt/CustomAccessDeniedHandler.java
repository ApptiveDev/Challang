package com.challang.backend.auth.jwt;

import com.challang.backend.auth.exception.AuthErrorCode;
import com.challang.backend.util.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // HTTP 상태 코드를 설정합니다. (403 Forbidden)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        BaseResponse<Object> errorResponse = new BaseResponse<>(AuthErrorCode.FORBIDDEN_ACCESS);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}