package shop.dontouch.dontouch_be.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;

@Getter
@NoArgsConstructor
public class SignupRequest {
  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @Size(max = 255, message = "이메일은 255자를 초과할 수 없습니다.")
  private String email;

  @NotBlank(message = "아이디는 필수입니다.")
  @Size(min = 4, max = 30, message = "아이디는 4자 이상 30자 이하이어야 합니다.")
  private String loginId;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, max = 72, message = "비밀번호는 8자 이상 72자 이하이어야 합니다.")
  private String password;

  @NotBlank(message = "닉네임은 필수입니다.")
  @Size(max = 30, message = "닉네임은 30자를 초과할 수 없습니다.")
  private String nickname;

  private Integer age;
  private UserGender gender;
  private UserJobType jobType;
  private UserRegion region;
}
