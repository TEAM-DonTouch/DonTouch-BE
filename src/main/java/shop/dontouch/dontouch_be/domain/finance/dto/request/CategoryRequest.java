package shop.dontouch.dontouch_be.domain.finance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {

  @NotBlank
  private String categoryName;

}
