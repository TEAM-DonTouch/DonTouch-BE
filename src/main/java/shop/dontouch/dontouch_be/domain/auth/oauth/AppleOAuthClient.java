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
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthSignupRequest;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Component
public class AppleOAuthClient {

  private static final String APPLE_JWK_SET_URI = "https://appleid.apple.com/auth/keys";
  private static final String APPLE_ISSUER = "https://appleid.apple.com";

  private final JwtDecoder jwtDecoder;
  private final String appleClientId;

  public AppleOAuthClient(
    @Value("${oauth.apple.client-id}") String appleClientId
  ) {
    this.appleClientId = appleClientId;

    NimbusJwtDecoder decoder = NimbusJwtDecoder
      .withJwkSetUri(APPLE_JWK_SET_URI)
      .build();

    decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(APPLE_ISSUER));

    this.jwtDecoder = decoder;
  }

  public SocialUserInfo getUserInfo(AppleOAuthLoginRequest request) {
    return getUserInfoByIdentityToken(request.getIdentityToken());
  }

  public SocialUserInfo getUserInfo(AppleOAuthSignupRequest request) {
    return getUserInfoByIdentityToken(request.getIdentityToken());
  }

  private SocialUserInfo getUserInfoByIdentityToken(String identityToken) {
    if (isBlank(identityToken)) {
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }

    try {
      Jwt jwt = jwtDecoder.decode(identityToken);

      validateAudience(jwt);

      String providerId = jwt.getSubject();
      String email = jwt.getClaimAsString("email");
      String nickname = jwt.getClaimAsString("name");

      if (isBlank(providerId) || isBlank(email)) {
        log.warn("apple oauth: 필수 사용자 정보 누락");
        throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
      }

      return new SocialUserInfo(
        providerId,
        email,
        nickname,
        null
      );
    } catch (JwtException e) {
      log.warn("apple oauth: identityToken 검증 실패", e);
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private void validateAudience(Jwt jwt) {
    List<String> audiences = jwt.getAudience();

    if (audiences == null || !audiences.contains(appleClientId)) {
      log.warn("apple oauth: audience 불일치");
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}