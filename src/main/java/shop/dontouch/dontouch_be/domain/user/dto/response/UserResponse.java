package shop.dontouch.dontouch_be.domain.user.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.user.constant.Gender;
import shop.dontouch.dontouch_be.domain.user.constant.JobType;
import shop.dontouch.dontouch_be.domain.user.constant.Region;
import shop.dontouch.dontouch_be.domain.user.constant.Role;
import shop.dontouch.dontouch_be.domain.user.constant.Status;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;

@Getter
@Builder
public class UserResponse {

  private UUID id;
  private String email;
  private String nickname;
  private String profileImageUrl;
  private Role userRole;
  private Integer age;
  private Gender gender;
  private JobType userJobType;
  private Region userRegion;
  private Status userStatus;

  public static UserResponse from(UserDto userDto) {
    return UserResponse.builder()
        .id(userDto.getId())
        .email(userDto.getEmail())
        .nickname(userDto.getNickname())
        .profileImageUrl(userDto.getProfileImageUrl())
        .userRole(userDto.getUserRole())
        .age(userDto.getAge())
        .gender(userDto.getGender())
        .userJobType(userDto.getUserJobType())
        .userRegion(userDto.getUserRegion())
        .userStatus(userDto.getUserStatus())
        .build();
  }
}
