package shop.dontouch.dontouch_be.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.global.common.BaseEntity;

@Entity
@Table(
    name = "user_settings",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_user_settings_user",
        columnNames = {"user_id"}
    )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserSettings extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(nullable = false)
  @Builder.Default
  private boolean pushNotificationEnabled = true;

  @Column(nullable = false)
  @Builder.Default
  private boolean biometricLoginEnabled = false;

  public void updateSettings(Boolean pushNotificationEnabled, Boolean biometricLoginEnabled) {
    if (pushNotificationEnabled != null) {
      this.pushNotificationEnabled = pushNotificationEnabled;
    }
    if (biometricLoginEnabled != null) {
      this.biometricLoginEnabled = biometricLoginEnabled;
    }
  }
}
