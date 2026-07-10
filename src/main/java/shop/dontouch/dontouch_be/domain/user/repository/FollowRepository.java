package shop.dontouch.dontouch_be.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.user.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, UUID> {

  Optional<Follow> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

  boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

  @Query("select f.following.id from Follow f where f.follower.id = :followerId")
  List<UUID> findFollowingIdsByFollowerId(@Param("followerId") UUID followerId);
}
