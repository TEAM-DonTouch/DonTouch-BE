package shop.dontouch.dontouch_be.domain.ai.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.ai.entity.AiAlert;
public interface AiAlertRepository extends JpaRepository<AiAlert, UUID> {

}
