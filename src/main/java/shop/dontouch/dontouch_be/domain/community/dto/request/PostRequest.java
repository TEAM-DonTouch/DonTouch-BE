package shop.dontouch.dontouch_be.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

  @Schema(description = "게시글 제목", example = "이번 달 절약 성공기")
  @NotBlank(message = "제목은 필수입니다.")
  @Size(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
  private String title;

  @Schema(description = "게시글 내용", example = "이번 달에 식비를 20% 줄였어요.")
  @NotBlank(message = "내용은 필수입니다.")
  @Size(max = 255, message = "내용은 255자를 초과할 수 없습니다.")
  private String content;
}
