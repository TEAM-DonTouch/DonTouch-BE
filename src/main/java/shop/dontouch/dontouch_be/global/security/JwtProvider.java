package shop.dontouch.dontouch_be.global.security;

import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.domain.user.entity.User;

@Component
public class JwtProvider {
  public String createAccessToken(User user) {
    // 일단 임시 토큰
    return "temporary-token";
  }
}
