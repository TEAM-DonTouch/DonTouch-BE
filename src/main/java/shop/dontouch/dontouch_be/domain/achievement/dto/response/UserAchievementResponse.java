package shop.dontouch.dontouch_be.domain.achievement.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.achievement.entity.UserAchievement;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementResponse {

  private UUID achievementId;
  private String name;
  private String description;
  private LocalDateTime earnedAt;

  public static UserAchievementResponse from(UserAchievement userAchievement) {
    return UserAchievementResponse.builder()
        .achievementId(userAchievement.getAchievement().getId())
        .name(userAchievement.getAchievement().getName())
        .description(userAchievement.getAchievement().getDescription())
        .earnedAt(userAchievement.getCreatedAt())
        .build();
  }
}
