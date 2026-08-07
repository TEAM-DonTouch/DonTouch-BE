package shop.dontouch.dontouch_be.domain.user.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.entity.UserSettings;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsResponse {

  private boolean pushNotificationEnabled;
  private boolean biometricLoginEnabled;
  private LocalDateTime updatedAt;

  public static UserSettingsResponse from(UserSettings userSettings) {
    return UserSettingsResponse.builder()
        .pushNotificationEnabled(userSettings.isPushNotificationEnabled())
        .biometricLoginEnabled(userSettings.isBiometricLoginEnabled())
        .updatedAt(userSettings.getUpdatedAt())
        .build();
  }
}
