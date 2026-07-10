package shop.dontouch.dontouch_be.domain.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeRequest {

  @Schema(description = "챌린지 제목", example = "일주일 무지출 챌린지")
  @NotBlank
  @Size(max = 50)
  private String title;

  @Schema(description = "챌린지 설명", example = "7일 동안 지출 없이 버텨보세요.")
  @Size(max = 255)
  private String description;

  @Schema(description = "목표치 (100% 달성 기준값)", example = "7")
  @NotNull
  @Positive
  private Integer targetValue;
}
