package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  boolean existsByName(String name);
}
