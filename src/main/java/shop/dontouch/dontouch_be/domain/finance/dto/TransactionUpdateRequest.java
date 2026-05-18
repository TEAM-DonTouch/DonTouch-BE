package shop.dontouch.dontouch_be.domain.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;

@Getter
@NoArgsConstructor
public class TransactionUpdateRequest {

  @Schema(description = "거래 금액", example = "50000")
  private Long amount;

  @Schema(description = "메모")
  @Size(max = 30)
  private String memo;

  @Schema(description = "거래 유형 (INCOME / EXPENSE)")
  private TransactionType type;

  @Schema(description = "거래 일시")
  private LocalDateTime transactionDate;

}