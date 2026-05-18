package shop.dontouch.dontouch_be.domain.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;

@Getter
@NoArgsConstructor
public class TransactionRequest {

  @Schema(description = "유저 ID", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  @NotNull
  private UUID userId;

  @Schema(description = "거래 금액", example = "50000")
  @NotNull
  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  @Schema(description = "거래 유형 (INCOME / EXPENSE)", example = "INCOME")
  @NotNull
  private TransactionType type; // INCOME,EXPENSE

  @Schema(description = "메모")
  @NotBlank
  @Size(max = 30)
  private String memo;

  @Schema(description = "거래 일시")
  @NotNull
  private LocalDateTime transactionDate;
}
