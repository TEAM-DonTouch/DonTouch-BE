package shop.dontouch.dontouch_be.domain.finance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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

  @Schema(description = "카테고리 ID", example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  @NotNull(message = "카테고리는 필수입니다.")
  private UUID categoryId;

  @Schema(description = "거래 유형 (INCOME / EXPENSE)", example = "INCOME")
  @NotNull(message = "거래 유형은 필수입니다.")
  private TransactionType type; // INCOME,EXPENSE

  @Schema(description = "거래 금액", example = "50000")
  @NotNull(message = "거래 금액은 필수입니다.")
  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  @Schema(description = "메모")
  @Size(max = 30, message = "메모는 30자를 초과할 수 없습니다.")
  private String memo;

  @Schema(description = "거래 일시")
  @NotNull(message = "거래 일시는 필수입니다.")
  private LocalDateTime transactionDate;
}
