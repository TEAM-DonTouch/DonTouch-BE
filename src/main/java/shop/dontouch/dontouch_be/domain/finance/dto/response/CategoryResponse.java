package shop.dontouch.dontouch_be.domain.finance.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;

@Getter
@Builder
public class CategoryResponse {

  private UUID categoryId;
  private String categoryName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CategoryResponse from(Category category) {
    return CategoryResponse.builder()
        .categoryId(category.getId())
        .categoryName(category.getName())
        .createdAt(category.getCreatedAt())
        .updatedAt(category.getUpdatedAt())
        .build();
  }

}
