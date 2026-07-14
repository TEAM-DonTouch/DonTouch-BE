package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleOAuthLoginRequest {

  @NotBlank(message = "애플 identityToken은 필수입니다.")
  private String identityToken;
}