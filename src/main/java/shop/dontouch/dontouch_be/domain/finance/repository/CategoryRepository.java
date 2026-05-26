package shop.dontouch.dontouch_be.domain.finance.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

  boolean existsByName(String name);

  boolean existsByNameAndIdNot(@NotBlank @Size(max = 10, message = "카테고리 이름은 최대 10자까지 입력 가능합니다.") String name, UUID id);

}
