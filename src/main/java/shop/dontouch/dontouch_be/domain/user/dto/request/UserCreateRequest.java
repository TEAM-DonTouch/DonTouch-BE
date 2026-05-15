package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

  private String email;
  private String nickname;
  private String profileImageUrl;
  private Integer age;
  private UserGender gender;
  private UserJobType userJobType;
  private UserRegion userRegion;

  public UserDto toDto() {
    return UserDto.builder()
        .email(email)
        .nickname(nickname)
        .profileImageUrl(profileImageUrl)
        .age(age)
        .gender(gender)
        .userJobType(userJobType)
        .userRegion(userRegion)
        .build();
  }
}
