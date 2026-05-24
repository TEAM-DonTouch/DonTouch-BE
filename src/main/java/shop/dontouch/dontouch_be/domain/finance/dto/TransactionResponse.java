package shop.dontouch.dontouch_be.domain.finance.dto;


import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

  private UUID transactionId;
  private UUID userId;
  private TransactionType type;
  private Long amount;
  private String memo;
  private LocalDateTime transactionDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TransactionResponse from(Transaction transaction) {
    return TransactionResponse.builder()
        .transactionId(transaction.getId())
        .userId(transaction.getUser().getId())
        .type(transaction.getType())
        .amount(transaction.getAmount())
        .memo(transaction.getMemo())
        .transactionDate(transaction.getTransactionDate())
        .createdAt(transaction.getCreatedAt())
        .updatedAt(transaction.getUpdatedAt())
        .build();
  }
}
