package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;

@Getter
@NoArgsConstructor
public class UserStatusUpdateRequest {

  private UserStatus userStatus;

  public UserDto toDto() {
    return UserDto.builder()
        .userStatus(userStatus)
        .build();
  }
}
