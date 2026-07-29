package shop.dontouch.dontouch_be.domain.achievement.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.achievement.entity.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

}
