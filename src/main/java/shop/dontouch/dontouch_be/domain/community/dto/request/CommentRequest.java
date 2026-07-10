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
  @NotBlank
  @Size(max = 100)
  private String content;
}
