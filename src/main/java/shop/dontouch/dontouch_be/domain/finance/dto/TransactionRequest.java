package shop.dontouch.dontouch_be.domain.finance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;
import shop.dontouch.dontouch_be.domain.user.entity.User;

@Getter
@NoArgsConstructor
public class TransactionRequest {

  @NotNull
  private UUID userId;

  private int amount;

  @NotNull
  private TransactionType type; // INCOME,EXPENSE
  
  private String memo;

  @NotNull
  private LocalDateTime transactionDate;

  public Transaction toEntity(User user) {
    return Transaction.builder()
        .user(user)
        .amount(amount)
        .type(type)
        .memo(memo)
        .transactionDate(transactionDate)
        .build();
  }


}
