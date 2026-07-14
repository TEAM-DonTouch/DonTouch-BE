package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleOAuthLoginRequest {

  @NotBlank(message = "구글 ID 토큰은 필수입니다.")
  private String idToken;
}
