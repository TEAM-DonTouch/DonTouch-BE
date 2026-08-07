package shop.dontouch.dontouch_be.domain.achievement.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.achievement.dto.response.UserAchievementResponse;
import shop.dontouch.dontouch_be.domain.achievement.service.AchievementService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController implements AchievementControllerDocs {

  private final AchievementService achievementService;

  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<List<UserAchievementResponse>> getMyAchievements(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(achievementService.getMyAchievements(currentUser.getUserId()));
  }
}
