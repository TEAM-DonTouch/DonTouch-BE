package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.Gender;
import shop.dontouch.dontouch_be.domain.user.constant.JobType;
import shop.dontouch.dontouch_be.domain.user.constant.Region;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

  private String nickname;
  private String profileImageUrl;
  private Integer age;
  private Gender gender;
  private JobType userJobType;
  private Region userRegion;

  public UserDto toDto() {
    return UserDto.builder()
        .nickname(nickname)
        .profileImageUrl(profileImageUrl)
        .age(age)
        .gender(gender)
        .userJobType(userJobType)
        .userRegion(userRegion)
        .build();
  }
}
