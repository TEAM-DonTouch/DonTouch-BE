package shop.dontouch.dontouch_be.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import shop.dontouch.dontouch_be.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
