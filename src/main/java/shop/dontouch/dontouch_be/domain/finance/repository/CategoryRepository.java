package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  boolean existsByNameAndUserIsNull(String name);

  boolean existsByNameAndUserIsNullAndIdNot(String name, UUID id);

  boolean existsByNameAndUserId(String name, UUID userId);

  boolean existsByNameAndUserIdAndIdNot(String name, UUID userId, UUID id);

  List<Category> findAllByUserIsNull();

  List<Category> findAllByUserId(UUID userId);

  @Query("SELECT c FROM Category c LEFT JOIN FETCH c.user WHERE c.user IS NULL OR c.user.id = :userId")
  List<Category> findAllVisibleToUser(@Param("userId") UUID userId);

}
