package com.challang.backend.user.entity;

import com.challang.backend.user.converter.UserRoleConverter;
import com.challang.backend.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "oauth_id", unique = true)
    private String oauthId;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Column(name = "nickname", unique = true)
    private String nickname;

    // 0: 남자, 1: 여자
    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Convert(converter = UserRoleConverter.class)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, String oauthId, String nickname, LocalDate birthDate, Integer gender,
                 UserRole role, AuthType authType) {
        this.email = email;
        this.password = password;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        this.authType = authType;
    }

    // 이메일 회원가입 시 사용
    public static User createWithEmail(String email, String password, String nickname, LocalDate birthDate, Integer gender,
                                       UserRole role) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .role(role)
                .authType(AuthType.EMAIL)
                .build();
    }

    // 카카오 회원가입 시 사용
    public static User createWithOauth(String oauthId, String nickname, LocalDate birthDate, Integer gender, UserRole role) {
        return User.builder()
                .oauthId(oauthId)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .role(role)
                .authType(AuthType.KAKAO)
                .build();
    }


}
