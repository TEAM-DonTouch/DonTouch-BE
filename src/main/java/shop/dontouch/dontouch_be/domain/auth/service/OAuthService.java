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
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.auth.dto.response.OAuthLoginResponse;
import shop.dontouch.dontouch_be.domain.auth.oauth.AppleOAuthClient;
import shop.dontouch.dontouch_be.domain.auth.oauth.GoogleOAuthClient;
import shop.dontouch.dontouch_be.domain.auth.oauth.KakaoOAuthClient;
import shop.dontouch.dontouch_be.domain.auth.oauth.SocialSignupInfo;
import shop.dontouch.dontouch_be.domain.auth.oauth.SocialUserInfo;
import shop.dontouch.dontouch_be.domain.auth.repository.RefreshTokenRedisRepository;
import shop.dontouch.dontouch_be.domain.auth.repository.SocialSignupTokenRedisRepository;
import shop.dontouch.dontouch_be.domain.user.constant.LoginPlatform;
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
public class OAuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final SocialSignupTokenRedisRepository socialSignupTokenRedisRepository;

  private final GoogleOAuthClient googleOAuthClient;
  private final KakaoOAuthClient kakaoOAuthClient;
  private final AppleOAuthClient appleOAuthClient;

  @Transactional
  public OAuthLoginResponse googleLogin(GoogleOAuthLoginRequest request) {
    SocialUserInfo socialUserInfo = googleOAuthClient.getUserInfo(request);
    validateSocialUserInfo(socialUserInfo);

    return loginOrCreateSignupToken(LoginPlatform.GOOGLE, socialUserInfo);
  }

  @Transactional
  public OAuthLoginResponse kakaoLogin(KakaoOAuthLoginRequest request) {
    SocialUserInfo socialUserInfo = kakaoOAuthClient.getUserInfo(request);
    validateSocialUserInfo(socialUserInfo);

    return loginOrCreateSignupToken(LoginPlatform.KAKAO, socialUserInfo);
  }

  @Transactional
  public OAuthLoginResponse appleLogin(AppleOAuthLoginRequest request) {
    SocialUserInfo socialUserInfo = appleOAuthClient.getUserInfo(request);
    validateSocialUserInfo(socialUserInfo);

    return loginOrCreateSignupToken(LoginPlatform.APPLE, socialUserInfo);
  }

  @Transactional
  public AuthResponse googleSignup(GoogleOAuthSignupRequest request) {
    SocialSignupInfo signupInfo = getValidSignupInfo(
      request.getSignupToken(),
      LoginPlatform.GOOGLE
    );

    User user = createSocialUser(
      LoginPlatform.GOOGLE,
      signupInfo.toSocialUserInfo(),
      request.getNickname()
    );

    socialSignupTokenRedisRepository.delete(request.getSignupToken());

    validateUserStatus(user);
    return issueTokens(user);
  }

  @Transactional
  public AuthResponse kakaoSignup(KakaoOAuthSignupRequest request) {
    SocialSignupInfo signupInfo = getValidSignupInfo(
      request.getSignupToken(),
      LoginPlatform.KAKAO
    );

    User user = createSocialUser(
      LoginPlatform.KAKAO,
      signupInfo.toSocialUserInfo(),
      request.getNickname()
    );

    socialSignupTokenRedisRepository.delete(request.getSignupToken());

    validateUserStatus(user);
    return issueTokens(user);
  }

  @Transactional
  public AuthResponse appleSignup(AppleOAuthSignupRequest request) {
    SocialSignupInfo signupInfo = getValidSignupInfo(
      request.getSignupToken(),
      LoginPlatform.APPLE
    );

    User user = createSocialUser(
      LoginPlatform.APPLE,
      signupInfo.toSocialUserInfo(),
      request.getNickname()
    );

    socialSignupTokenRedisRepository.delete(request.getSignupToken());

    validateUserStatus(user);
    return issueTokens(user);
  }

  private OAuthLoginResponse loginOrCreateSignupToken(
    LoginPlatform loginPlatform,
    SocialUserInfo socialUserInfo
  ) {
    return userRepository
      .findByLoginPlatformAndProviderId(loginPlatform, socialUserInfo.providerId())
      .map(user -> {
        validateUserStatus(user);
        return OAuthLoginResponse.success(issueTokens(user));
      })
      .orElseGet(() -> {
        String signupToken = UUID.randomUUID().toString();

        socialSignupTokenRedisRepository.save(
          signupToken,
          SocialSignupInfo.from(loginPlatform, socialUserInfo)
        );

        return OAuthLoginResponse.signupRequired(loginPlatform, signupToken);
      });
  }

  private SocialSignupInfo getValidSignupInfo(
    String signupToken,
    LoginPlatform expectedLoginPlatform
  ) {
    SocialSignupInfo signupInfo = socialSignupTokenRedisRepository
      .findByToken(signupToken)
      .orElseThrow(() -> new CustomException(ErrorCode.SOCIAL_SIGNUP_TOKEN_INVALID));

    if (signupInfo.loginPlatform() != expectedLoginPlatform) {
      throw new CustomException(ErrorCode.SOCIAL_SIGNUP_TOKEN_INVALID);
    }

    return signupInfo;
  }

  private User createSocialUser(
    LoginPlatform loginPlatform,
    SocialUserInfo socialUserInfo,
    String nickname
  ) {
    userRepository
      .findByLoginPlatformAndProviderId(loginPlatform, socialUserInfo.providerId())
      .ifPresent(user -> {
        log.warn("social signup: 이미 가입된 소셜 계정 loginPlatform={}", loginPlatform);
        throw new CustomException(ErrorCode.USER_DUPLICATE);
      });

    if (userRepository.existsByEmail(socialUserInfo.email())) {
      log.warn("social signup: 이미 가입된 이메일 loginPlatform={}", loginPlatform);
      throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
    }

    if (userRepository.existsByNickname(nickname)) {
      log.warn("social signup: 중복 닉네임 loginPlatform={}", loginPlatform);
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    User user = User.builder()
      .email(socialUserInfo.email())
      .nickname(nickname)
      .profileImageUrl(socialUserInfo.profileImageUrl())
      .loginId(makeDummyLoginId(loginPlatform, socialUserInfo.providerId()))
      .password(passwordEncoder.encode(UUID.randomUUID().toString()))
      .loginPlatform(loginPlatform)
      .providerId(socialUserInfo.providerId())
      .gender(UserGender.NOT_SELECTED)
      .jobType(UserJobType.OTHER)
      .region(UserRegion.SEOUL)
      .build();

    try {
      return userRepository.saveAndFlush(user);
    } catch (DataIntegrityViolationException e) {
      log.warn("social signup: DB 제약조건 위반", e);
      throw new CustomException(ErrorCode.USER_DUPLICATE);
    }
  }

  private void validateSocialUserInfo(SocialUserInfo socialUserInfo) {
    if (socialUserInfo == null
        || isBlank(socialUserInfo.providerId())
        || isBlank(socialUserInfo.email())) {
      log.warn("social login/signup: 소셜 사용자 정보 누락");
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private void validateUserStatus(User user) {
    if (user.getStatus() == UserStatus.SUSPENDED) {
      throw new CustomException(ErrorCode.USER_SUSPENDED);
    }

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      throw new CustomException(ErrorCode.USER_WITHDRAWN);
    }
  }

  private AuthResponse issueTokens(User user) {
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

  private String makeDummyLoginId(LoginPlatform loginPlatform, String providerId) {
    String prefix = loginPlatform.name().toLowerCase() + "_";
    String hashedProviderId = hashValue(providerId).substring(0, 20);

    return prefix + hashedProviderId;
  }

  private String hashRefreshToken(String refreshToken) {
    return hashValue(refreshToken);
  }

  private String hashValue(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 algorithm not available", e);
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}