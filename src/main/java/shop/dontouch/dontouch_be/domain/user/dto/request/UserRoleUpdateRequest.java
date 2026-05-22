package shop.dontouch.dontouch_be.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;

@Getter
@NoArgsConstructor
public class UserRoleUpdateRequest {
  @NotNull(message = "사용자 역할은 필수입니다")
  private UserRole userRole;
}
