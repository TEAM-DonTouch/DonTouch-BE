package shop.dontouch.dontouch_be.domain.user.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;

@Getter
@Builder
public class UserResponse {

  private UUID id;
  private String email;
  private String nickname;
  private String profileImageUrl;
  private UserRole userRole;
  private Integer age;
  private UserGender gender;
  private UserJobType userJobType;
  private UserRegion userRegion;
  private UserStatus userStatus;

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
