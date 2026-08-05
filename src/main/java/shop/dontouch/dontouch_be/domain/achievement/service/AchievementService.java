package shop.dontouch.dontouch_be.domain.achievement.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.achievement.dto.response.UserAchievementResponse;
import shop.dontouch.dontouch_be.domain.achievement.repository.UserAchievementRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementService {

  private final UserAchievementRepository userAchievementRepository;

  public List<UserAchievementResponse> getMyAchievements(UUID userId) {
    return userAchievementRepository.findAllByUserIdWithAchievement(userId).stream()
        .map(UserAchievementResponse::from)
        .toList();
  }
}
