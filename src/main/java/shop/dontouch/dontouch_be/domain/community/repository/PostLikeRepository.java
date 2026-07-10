package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.community.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

  Optional<PostLike> findByUserIdAndPostId(UUID userId, UUID postId);

  boolean existsByUserIdAndPostId(UUID userId, UUID postId);

  @Query("select pl.post.id from PostLike pl where pl.user.id = :userId and pl.post.id in :postIds")
  List<UUID> findLikedPostIdsByUserIdAndPostIdIn(
      @Param("userId") UUID userId,
      @Param("postIds") List<UUID> postIds
  );
}
