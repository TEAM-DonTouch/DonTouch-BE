package shop.dontouch.dontouch_be.domain.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.auth.dto.request.LoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.RefreshTokenRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.SignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.auth.repository.RefreshTokenRedisRepository;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import shop.dontouch.dontouch_be.global.security.JwtProvider;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;

  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if (userRepository.existsByLoginId(request.getLoginId())) {
      log.warn("signup: 중복 loginId");
      throw new CustomException(ErrorCode.USER_LOGIN_ID_DUPLICATE);
    }
    if (userRepository.existsByEmail(request.getEmail())) {
      log.warn("signup: 중복 email");
      throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
    }
    if (userRepository.existsByNickname(request.getNickname())) {
      log.warn("signup: 중복 nickname");
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    User user = User.builder()
        .email(request.getEmail())
        .loginId(request.getLoginId())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .age(request.getAge())
        .gender(request.getGender() != null ? request.getGender() : UserGender.NOT_SELECTED)
        .jobType(request.getJobType() != null ? request.getJobType() : UserJobType.OTHER)
        .region(request.getRegion() != null ? request.getRegion() : UserRegion.SEOUL)
        .build();

    User savedUser;
    try {
      savedUser = userRepository.saveAndFlush(user);
    } catch (DataIntegrityViolationException e) {
      log.warn("signup: DB 제약조건 위반");
      throw new CustomException(ErrorCode.USER_DUPLICATE);
    }

    String accessToken = jwtProvider.createAccessToken(savedUser);
    String refreshToken = jwtProvider.createRefreshToken();
    String refreshTokenKey = hashRefreshToken(refreshToken);

    refreshTokenRedisRepository.save(
        refreshTokenKey,
        savedUser.getId().toString(),
        jwtProvider.getRefreshTokenExpiration()
    );

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .user(UserResponse.from(savedUser))
        .build();
  }

  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> {
          log.warn("login: 존재하지 않는 loginId");
          return new CustomException(ErrorCode.LOGIN_FAILED);
        });

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      log.warn("login: 비밀번호 불일치");
      throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
    if (user.getStatus() == UserStatus.SUSPENDED) {
      log.warn("login: 정지된 유저");
      throw new CustomException(ErrorCode.USER_SUSPENDED);
    }
    if (user.getStatus() == UserStatus.WITHDRAWN) {
      log.warn("login: 탈퇴한 유저");
      throw new CustomException(ErrorCode.USER_WITHDRAWN);
    }

    String accessToken = jwtProvider.createAccessToken(user);
    String refreshToken = jwtProvider.createRefreshToken();
    String refreshTokenKey = hashRefreshToken(refreshToken);

    refreshTokenRedisRepository.save(
        refreshTokenKey,
        user.getId().toString(),
        jwtProvider.getRefreshTokenExpiration()
    );

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .user(UserResponse.from(user))
        .build();
  }

  public void logout(RefreshTokenRequest request) {
    String refreshTokenKey = hashRefreshToken(request.getRefreshToken());

    boolean deleted = refreshTokenRedisRepository.delete(refreshTokenKey);

    if (!deleted) {
      log.warn("logout: 존재하지 않거나 만료된 refreshToken");
      throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
  }

  public AuthResponse refresh(RefreshTokenRequest request) {
    String oldRefreshTokenKey = hashRefreshToken(request.getRefreshToken());

    String newRefreshToken = jwtProvider.createRefreshToken();
    String newRefreshTokenKey = hashRefreshToken(newRefreshToken);

    String userIdValue = refreshTokenRedisRepository.rotate(
        oldRefreshTokenKey,
        newRefreshTokenKey,
        jwtProvider.getRefreshTokenExpiration()
    ).orElseThrow(() -> {
      log.warn("refresh: 존재하지 않거나 만료된 refreshToken");
      return new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    });

    UUID userId = UUID.fromString(userIdValue);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          refreshTokenRedisRepository.delete(newRefreshTokenKey);
          log.warn("refresh: refreshToken의 userId에 해당하는 유저 없음");
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    if (user.getStatus() == UserStatus.SUSPENDED) {
      refreshTokenRedisRepository.delete(newRefreshTokenKey);
      log.warn("refresh: 정지된 유저의 refreshToken");
      throw new CustomException(ErrorCode.USER_SUSPENDED);
    }

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      refreshTokenRedisRepository.delete(newRefreshTokenKey);
      log.warn("refresh: 탈퇴한 유저의 refreshToken");
      throw new CustomException(ErrorCode.USER_WITHDRAWN);
    }

    String newAccessToken = jwtProvider.createAccessToken(user);

    return AuthResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .user(UserResponse.from(user))
        .build();
  }

  private String hashRefreshToken(String refreshToken) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 algorithm not available", e);
    }
  }
}
