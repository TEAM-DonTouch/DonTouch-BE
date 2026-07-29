package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenRequest {

  @NotBlank(message = "리프레시 토큰은 필수입니다.")
  private String refreshToken;

}
