package shop.dontouch.dontouch_be.domain.auth.oauth;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthLoginRequest;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Component
public class GoogleOAuthClient {

  private static final String GOOGLE_JWK_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";
  private static final String GOOGLE_ISSUER_HTTPS = "https://accounts.google.com";
  private static final String GOOGLE_ISSUER = "accounts.google.com";

  private final JwtDecoder jwtDecoder;
  private final String googleClientId;

  public GoogleOAuthClient(
    @Value("${oauth.google.client-id}") String googleClientId
  ) {
    this.googleClientId = googleClientId;

    NimbusJwtDecoder decoder = NimbusJwtDecoder
      .withJwkSetUri(GOOGLE_JWK_SET_URI)
      .build();

    decoder.setJwtValidator(JwtValidators.createDefault());

    this.jwtDecoder = decoder;
  }

  private SocialUserInfo getUserInfoByIdToken(String idToken) {
    if (isBlank(idToken)) {
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }

    try {
      Jwt jwt = jwtDecoder.decode(idToken);

      validateIssuer(jwt);
      validateAudience(jwt);

      String providerId = jwt.getSubject();
      String email = jwt.getClaimAsString("email");
      String nickname = jwt.getClaimAsString("name");
      String profileImageUrl = jwt.getClaimAsString("picture");

      if (isBlank(providerId) || isBlank(email)) {
        log.warn("google oauth: 필수 사용자 정보 누락");
        throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
      }

      if (!isEmailVerified(jwt)) {
        log.warn("google oauth: 이메일 인증되지 않음");
        throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
      }

      return new SocialUserInfo(
        providerId,
        email,
        nickname,
        profileImageUrl
      );
    } catch (JwtException e) {
      log.warn("google oauth: idToken 검증 실패", e);
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  public SocialUserInfo getUserInfo(GoogleOAuthLoginRequest request) {
    return getUserInfoByIdToken(request.getIdToken());
  }

  private void validateIssuer(Jwt jwt) {
    String issuer = jwt.getIssuer() == null ? null : jwt.getIssuer().toString();

    if (!GOOGLE_ISSUER_HTTPS.equals(issuer) && !GOOGLE_ISSUER.equals(issuer)) {
      log.warn("google oauth: issuer 불일치 issuer={}", issuer);
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private void validateAudience(Jwt jwt) {
    List<String> audiences = jwt.getAudience();

    if (audiences == null || !audiences.contains(googleClientId)) {
      log.warn("google oauth: audience 불일치");
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private boolean isEmailVerified(Jwt jwt) {
    Object emailVerified = jwt.getClaims().get("email_verified");

    return Boolean.TRUE.equals(emailVerified)
           || "true".equals(String.valueOf(emailVerified));
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}