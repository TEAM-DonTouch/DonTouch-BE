package shop.dontouch.dontouch_be.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSettingsUpdateRequest {

  @Schema(description = "푸시 알림 수신 여부")
  private Boolean pushNotificationEnabled;

  @Schema(description = "생체 인증 로그인 사용 여부")
  private Boolean biometricLoginEnabled;
}
