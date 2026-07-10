package shop.dontouch.dontouch_be.domain.community.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.community.entity.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

  Page<Post> findAllByUserIdIn(List<UUID> userIds, Pageable pageable);
}
