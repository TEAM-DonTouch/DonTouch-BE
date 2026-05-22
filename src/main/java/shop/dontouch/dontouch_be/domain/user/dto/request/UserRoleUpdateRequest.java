package shop.dontouch.dontouch_be.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;

@Getter
@NoArgsConstructor
public class UserRoleUpdateRequest {

  private UserRole userRole;
}
