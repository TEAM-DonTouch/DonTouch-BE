package shop.dontouch.dontouch_be.domain.finance.dto;


import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

  private UUID transactionId;
  private UUID userId;
  private TransactionType type;
  private int amount;
  private String memo;
  private LocalDateTime transactionDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;

  public static TransactionResponse from(Transaction transaction) {
    return new TransactionResponse(
        transaction.getId(),
        transaction.getUser().getId(),
        transaction.getType(),
        transaction.getAmount(),
        transaction.getMemo(),
        transaction.getTransactionDate(),
        transaction.getCreatedAt(),
        transaction.getUpdatedAt(),
        transaction.getDeletedAt()
    );
  }

}
