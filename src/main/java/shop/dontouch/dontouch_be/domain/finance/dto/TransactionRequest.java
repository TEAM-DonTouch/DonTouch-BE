package shop.dontouch.dontouch_be.domain.finance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

  @NotNull
  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  @NotNull
  private TransactionType type; // INCOME,EXPENSE

  @NotBlank
  @Size(max = 30)
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
