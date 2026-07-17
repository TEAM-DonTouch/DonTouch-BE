package shop.dontouch.dontouch_be.domain.auth.oauth;

import shop.dontouch.dontouch_be.domain.user.constant.LoginPlatform;

public record SocialSignupInfo(
  LoginPlatform loginPlatform,
  String providerId,
  String email,
  String nickname,
  String profileImageUrl
) {

  public static SocialSignupInfo from(
    LoginPlatform loginPlatform,
    SocialUserInfo socialUserInfo
  ) {
    return new SocialSignupInfo(
      loginPlatform,
      socialUserInfo.providerId(),
      socialUserInfo.email(),
      socialUserInfo.nickname(),
      socialUserInfo.profileImageUrl()
    );
  }

  public SocialUserInfo toSocialUserInfo() {
    return new SocialUserInfo(
      providerId,
      email,
      nickname,
      profileImageUrl
    );
  }
}