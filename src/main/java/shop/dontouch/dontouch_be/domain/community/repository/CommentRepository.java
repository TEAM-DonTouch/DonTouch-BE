package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.community.entity.Comment;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  Page<Comment> findAllByPostIdOrderByCreatedAtAsc(UUID postId, Pageable pageable);
}
