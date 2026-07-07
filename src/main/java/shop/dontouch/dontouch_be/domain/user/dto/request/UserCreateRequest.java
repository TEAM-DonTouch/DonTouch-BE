package shop.dontouch.dontouch_be.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

  @NotBlank(message = "아이디는 필수입니다.")
  @Size(min = 4, max = 30, message = "아이디는 4자 이상 30자 이하이어야 합니다.")
  private String loginId;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Pattern(regexp = "^[\\x21-\\x7E]+$", message = "비밀번호는 공백 없이 영문, 숫자, 특수문자만 사용할 수 있습니다.")
  @Size(min = 8, max = 72, message = "비밀번호는 8자 이상 72자 이하이어야 합니다.")
  private String password;

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @Size(max = 255, message = "이메일은 255자를 초과할 수 없습니다.")
  private String email;

  @NotBlank(message = "닉네임은 필수입니다.")
  @Size(max = 30, message = "닉네임은 30자를 초과할 수 없습니다.")
  private String nickname;

  @Size(max = 500, message = "프로필 이미지 URL은 500자를 초과할 수 없습니다.")
  private String profileImageUrl;

  @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
  private Integer age;

  private UserGender gender;

  private UserJobType userJobType;

  private UserRegion userRegion;
}
