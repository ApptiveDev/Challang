package com.challang.backend.auth.service;

import com.challang.backend.auth.dto.request.*;
import com.challang.backend.auth.dto.response.*;
import com.challang.backend.auth.jwt.CustomUserDetails;
import com.challang.backend.auth.jwt.JwtUtil;
import com.challang.backend.global.exception.BaseException;
import com.challang.backend.global.util.RedisUtil;
import com.challang.backend.preference.repository.LiquorPreferenceLevelRepository;
import com.challang.backend.preference.repository.LiquorPreferenceTagRepository;
import com.challang.backend.preference.repository.LiquorPreferenceTypeRepository;
import com.challang.backend.user.entity.User;
import com.challang.backend.user.exception.UserErrorCode;
import com.challang.backend.user.repository.UserRepository;
import com.challang.backend.user.service.UserService;
import com.challang.backend.withdrawal.entity.ReasonType;
import com.challang.backend.withdrawal.entity.WithdrawalReason;
import com.challang.backend.withdrawal.repository.WithdrawalReasonRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AuthService {

    private static final int ADULT_AGE = 18;

    private final KakaoService kakaoService;

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserService userService;

    private final WithdrawalReasonRepository withdrawalReasonRepository;
    private final LiquorPreferenceLevelRepository preferenceLevelRepository;
    private final LiquorPreferenceTypeRepository preferenceTypeRepository;
    private final LiquorPreferenceTagRepository preferenceTagRepository;

    @Value("${jwt.refresh-expired-time}")
    private long refreshExpiredSeconds;

    // 이메일 회원가입
//    public boolean emailSignUp(EmailSignUpRequestDto dto) {
//        validateAdult(dto.birthDate());
//
//        if (existsByEmail(dto.email())) {
//            throw new BaseException(UserErrorCode.USER_ALREADY_EXISTS);
//        }
//
//        String encodedPassword = passwordEncoder.encode(dto.password());
//
//        User user = User.createWithEmail(
//                dto.email(),
//                encodedPassword,
//                dto.nickname(),
//                dto.birthDate(),
//                dto.gender(),
//                dto.role()
//        );
//
//        userRepository.save(user);
//
//        return true;
//    }
//
//    // 이메일 로그인
//    @Transactional
//    public TokenResponse emailSignIn(EmailSignInRequestDto dto) {
//        // 인증
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
//
//        // 토큰 생성
//        String accessToken = jwtUtil.createAccessToken(authentication);
//        String refreshToken = jwtUtil.createRefreshToken(authentication);
//
//        // 리프레시 토큰의 jti 추출
//        String jti = jwtUtil.getJti(refreshToken);
//
//        // redis에 저장
//        redisUtil.setDataExpire(jti, refreshToken, refreshExpiredSeconds);
//
//        return new TokenResponse(accessToken, refreshToken);
//    }

    public boolean kakaoSignUp(KakaoSignUpRequestDto dto) {
        // 성인인지 확인
        validateAdult(dto.birthDate());

        // 유저 정보 받기
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(dto.accessToken());
        String oauthId = userInfo.id().toString();

        if (existsByOauthId(oauthId)) {
            throw new BaseException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.createWithOauth(
                oauthId,
                dto.nickname(),
                dto.birthDate(),
                dto.gender(),
                dto.role()
        );

        // db에 저장
        userRepository.save(user);

        return true;
    }

    public TokenResponse kakaoSignIn(String accessToken) {
        // 1. Kakao 사용자 정보 조회
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        String oauthId = userInfo.id().toString();

        // 2. 유저 조회
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND, "등록되지 않은 카카오 계정입니다."));

        // 3. 인증 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

        // 4. 토큰 생성
        String accessJwt = jwtUtil.createAccessToken(authentication);
        String refreshJwt = jwtUtil.createRefreshToken(authentication);

        // 5. Redis 저장
        String jti = jwtUtil.getJti(refreshJwt);
        redisUtil.setDataExpire(jti, refreshJwt, refreshExpiredSeconds);

        // 6. 선호 설정 여부 확인
        boolean isPreferenceSet =
                preferenceLevelRepository.existsByUser(user)
                        && preferenceTypeRepository.existsByUser(user)
                        && preferenceTagRepository.existsByUser(user);

        return new TokenResponse(accessJwt, refreshJwt, isPreferenceSet);
    }

    public TokenResponse reissueTokens(HttpServletRequest request) {
        String oldRefresh = jwtUtil.resolveRefreshToken(request);

        // 기존 토큰 무효화
        String oldJti = jwtUtil.getJti(oldRefresh);
        redisUtil.deleteData(oldJti);

        // access token 재발급
        String newAccess = jwtUtil.createTokenFromRefreshToken(oldRefresh);

        // 새 리프레시 토큰 발급
        Long userId = jwtUtil.getId(oldRefresh);
        UserDetails userDetails = userDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        String newRefresh = jwtUtil.createRefreshToken(auth);

        // 새 리프레시 토큰 저장
        String newJti = jwtUtil.getJti(newRefresh);
        redisUtil.setDataExpire(newJti, newRefresh, refreshExpiredSeconds);

        return new TokenResponse(newAccess, newRefresh);
    }


    public boolean logout(String token) {
        try {
            String jti = jwtUtil.getJti(token);
            redisUtil.deleteData(jti);
            return true;
        } catch (Exception e) {
            log.error("로그아웃 실패: {}", e.getMessage(), e);
            return false;
        }
    }


    @Transactional
    public void deleteUser(final Long id, String refreshToken, ReasonType reason) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        WithdrawalReason withdrawalReason = WithdrawalReason.builder()
                .userId(user.getUserId())
                .reason(reason)
                .build();
        withdrawalReasonRepository.save(withdrawalReason);

        try {
            String jti = jwtUtil.getJti(refreshToken);
            redisUtil.deleteData(jti);
        } catch (Exception e) {
            log.warn("유저 삭제 중 토큰 제거 실패: {}", e.getMessage());
        }

        userService.deleteUser(id);
    }


    private void validateAdult(LocalDate birthDate) {
        if (birthDate.plusYears(ADULT_AGE).isAfter(LocalDate.now())) {
            throw new BaseException(UserErrorCode.UNDERAGE_SIGNUP_NOT_ALLOWED);
        }
    }


    private boolean existsByOauthId(String oauthId) {
        return userRepository.existsByOauthId(oauthId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
