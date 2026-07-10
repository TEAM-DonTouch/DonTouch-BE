package shop.dontouch.dontouch_be.domain.challenge.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.challenge.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {

}
