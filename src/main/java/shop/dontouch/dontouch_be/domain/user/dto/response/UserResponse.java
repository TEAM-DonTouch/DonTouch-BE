package shop.dontouch.dontouch_be.domain.user.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.entity.User;

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

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .profileImageUrl(user.getProfileImageUrl())
        .userRole(user.getRole())
        .age(user.getAge())
        .gender(user.getGender())
        .userJobType(user.getJobType())
        .userRegion(user.getRegion())
        .userStatus(user.getStatus())
        .build();
  }
}
