package shop.dontouch.dontouch_be.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;

@Getter
@Builder
public class AuthResponse {
  private String accessToken;
  private String tokenType;
  private UserResponse user;
}
