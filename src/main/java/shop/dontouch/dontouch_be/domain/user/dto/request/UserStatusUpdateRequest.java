package shop.dontouch.dontouch_be.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;

@Getter
@NoArgsConstructor
public class UserStatusUpdateRequest {
  @NotNull(message = "사용자 상태는 필수입니다")
  private UserStatus userStatus;
}
