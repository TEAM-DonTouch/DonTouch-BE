package shop.dontouch.dontouch_be.domain.finance.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;

@Getter
@NoArgsConstructor
public class TransactionUpdateRequest {

  private Long amount;
  private String memo;
  private TransactionType type;
  private LocalDateTime transactionDate;

}