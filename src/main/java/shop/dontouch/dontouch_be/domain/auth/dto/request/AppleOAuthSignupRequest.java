package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleOAuthSignupRequest {

  @NotBlank(message = "애플 identityToken은 필수입니다.")
  private String identityToken;

  @NotBlank(message = "닉네임은 필수입니다.")
  @Size(max = 30)
  private String nickname;
}