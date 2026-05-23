package shop.dontouch.dontouch_be.domain.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

  @Size(max = 30, message = "닉네임은 30자를 초과할 수 없습니다.")
  @Pattern(regexp = "^(?!\\s*$).*$", message = "닉네임은 공백만 입력할 수 없습니다.")
  private String nickname;

  @Size(max = 500, message = "프로필 이미지 URL은 500자를 초과할 수 없습니다.")
  private String profileImageUrl;

  @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
  private Integer age;

  private UserGender gender;

  private UserJobType userJobType;

  private UserRegion userRegion;
}
