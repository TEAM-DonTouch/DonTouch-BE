package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoOAuthSignupRequest {

  @NotBlank(message = "카카오 인가 코드는 필수입니다.")
  private String code;

  @NotBlank(message = "닉네임은 필수입니다.")
  @Size(max = 30)
  private String nickname;
}