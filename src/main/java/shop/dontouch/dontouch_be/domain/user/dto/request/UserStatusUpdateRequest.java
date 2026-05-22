package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;

@Getter
@NoArgsConstructor
public class UserStatusUpdateRequest {

  private UserStatus userStatus;
}
