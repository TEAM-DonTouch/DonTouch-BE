package shop.dontouch.dontouch_be.domain.achievement.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.achievement.entity.UserAchievement;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UUID> {

  @Query("select ua from UserAchievement ua join fetch ua.achievement where ua.user.id = :userId order by ua.createdAt desc")
  List<UserAchievement> findAllByUserIdWithAchievement(@Param("userId") UUID userId);
}
