package shop.dontouch.dontouch_be.domain.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.user.entity.UserSettings;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {

  Optional<UserSettings> findByUserId(UUID userId);

  boolean existsByUserId(UUID userId);
}
