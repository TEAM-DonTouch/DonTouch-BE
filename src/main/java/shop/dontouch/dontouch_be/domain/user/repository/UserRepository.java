package shop.dontouch.dontouch_be.domain.user.repository;


import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByNicknameAndIdNot(String nickname, UUID id);
  boolean existsByEmail(String email);
}
