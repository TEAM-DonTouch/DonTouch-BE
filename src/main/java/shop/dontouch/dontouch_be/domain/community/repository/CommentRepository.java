package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.community.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  List<Comment> findAllByPostIdOrderByCreatedAtAsc(UUID postId);
}
