package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoOAuthLoginRequest {

  @NotBlank(message = "카카오 인가 코드는 필수입니다.")
  private String code;
}
