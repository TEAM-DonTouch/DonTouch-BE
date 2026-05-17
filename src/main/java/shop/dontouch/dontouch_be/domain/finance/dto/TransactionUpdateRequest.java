package shop.dontouch.dontouch_be.domain.finance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;

@Getter
@NoArgsConstructor
public class TransactionUpdateRequest {

  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  @Size(max = 30)
  private String memo;

  private TransactionType type;

  private LocalDateTime transactionDate;

}