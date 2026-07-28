package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.community.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  @EntityGraph(attributePaths = "user")
  Page<Comment> findAllByPostId(UUID postId, Pageable pageable);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = :commentId AND c.deletedAt IS NULL")
  int softDelete(@Param("commentId") UUID commentId);

}
