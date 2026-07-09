package shop.dontouch.dontouch_be.domain.auth.oauth;

public record SocialUserInfo(
  String providerId,
  String email,
  String nickname,
  String profileImageUrl
) {
}
