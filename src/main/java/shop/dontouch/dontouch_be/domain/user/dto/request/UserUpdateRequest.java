package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

  private String nickname;
  private String profileImageUrl;
  private Integer age;
  private UserGender gender;
  private UserJobType userJobType;
  private UserRegion userRegion;
}
