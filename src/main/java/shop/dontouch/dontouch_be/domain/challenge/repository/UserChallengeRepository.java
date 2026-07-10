package shop.dontouch.dontouch_be.domain.challenge.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.challenge.entity.UserChallenge;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, UUID> {

  Optional<UserChallenge> findByUserIdAndChallengeId(UUID userId, UUID challengeId);

  @Query("select uc from UserChallenge uc join fetch uc.challenge where uc.user.id = :userId")
  List<UserChallenge> findAllByUserIdWithChallenge(@Param("userId") UUID userId);

  @Query("select uc from UserChallenge uc where uc.user.id = :userId and uc.challenge.id in :challengeIds")
  List<UserChallenge> findAllByUserIdAndChallengeIdIn(
      @Param("userId") UUID userId,
      @Param("challengeIds") List<UUID> challengeIds
  );
}
