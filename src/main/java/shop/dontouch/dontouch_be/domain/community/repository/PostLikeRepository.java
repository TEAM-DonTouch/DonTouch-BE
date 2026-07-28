package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.community.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

  boolean existsByUserIdAndPostId(UUID userId, UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("DELETE FROM PostLike pl WHERE pl.user.id = :userId AND pl.post.id = :postId")
  int deleteByUserIdAndPostId(@Param("userId") UUID userId, @Param("postId") UUID postId);

  @Query("select pl.post.id from PostLike pl where pl.user.id = :userId and pl.post.id in :postIds")
  List<UUID> findLikedPostIdsByUserIdAndPostIdIn(
      @Param("userId") UUID userId,
      @Param("postIds") List<UUID> postIds
  );
}
