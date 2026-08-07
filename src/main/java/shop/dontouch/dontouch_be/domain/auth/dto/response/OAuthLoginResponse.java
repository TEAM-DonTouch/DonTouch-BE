package shop.dontouch.dontouch_be.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.user.constant.LoginPlatform;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;

@Getter
@Builder
public class OAuthLoginResponse {

  private boolean signupRequired;
  private LoginPlatform loginPlatform;
  private String signupToken;

  private String accessToken;
  private String refreshToken;
  private UserResponse user;

  public static OAuthLoginResponse success(AuthResponse authResponse) {
    return OAuthLoginResponse.builder()
      .signupRequired(false)
      .accessToken(authResponse.getAccessToken())
      .refreshToken(authResponse.getRefreshToken())
      .user(authResponse.getUser())
      .build();
  }

  public static OAuthLoginResponse signupRequired(
    LoginPlatform loginPlatform,
    String signupToken
  ) {
    return OAuthLoginResponse.builder()
      .signupRequired(true)
      .loginPlatform(loginPlatform)
      .signupToken(signupToken)
      .build();
  }
}