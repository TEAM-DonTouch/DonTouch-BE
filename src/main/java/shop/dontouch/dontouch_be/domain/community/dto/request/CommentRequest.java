package shop.dontouch.dontouch_be.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {

  @Schema(description = "댓글 내용", example = "저도 이번 달에 도전해볼게요!")
  @NotBlank(message = "댓글 내용은 필수입니다.")
  @Size(max = 100, message = "댓글은 100자를 초과할 수 없습니다.")
  private String content;
}
