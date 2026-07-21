package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

  @EntityGraph(attributePaths = "user")
  @Query("SELECT p FROM Post p")
  Page<Post> findAllWithUser(Pageable pageable);

  @EntityGraph(attributePaths = "user")
  @Query("SELECT p FROM Post p WHERE p.id = :postId")
  Optional<Post> findByIdWithUser(@Param("postId") UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
  int increaseViewCount(@Param("postId") UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
  void increaseLikeCount(@Param("postId") UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
      UPDATE Post p
      SET p.likeCount = CASE
          WHEN p.likeCount > 0 THEN p.likeCount - 1
          ELSE 0
      END
      WHERE p.id = :postId
      """)
  void decreaseLikeCount(@Param("postId") UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE Post p SET p.commentCount = p.commentCount + 1 WHERE p.id = :postId")
  void increaseCommentCount(@Param("postId") UUID postId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
      UPDATE Post p
      SET p.commentCount = CASE
          WHEN p.commentCount > 0 THEN p.commentCount - 1
          ELSE 0
      END
      WHERE p.id = :postId
      """)
  void decreaseCommentCount(@Param("postId") UUID postId);
}