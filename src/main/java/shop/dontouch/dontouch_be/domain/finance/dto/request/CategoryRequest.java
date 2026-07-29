package shop.dontouch.dontouch_be.domain.finance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {

  @NotBlank(message = "카테고리 이름은 필수입니다.")
  @Size(max = 10, message = "카테고리 이름은 최대 10자까지 입력 가능합니다.")
  private String categoryName;

}
