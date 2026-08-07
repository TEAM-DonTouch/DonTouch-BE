package shop.dontouch.dontouch_be.domain.finance.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;
import shop.dontouch.dontouch_be.domain.finance.entity.Budget;

public record BudgetResponse(
    UUID budgetId,
    UUID userId,
    BudgetPeriod period,
    Long amount,
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long usedAmount,
    Long remainingAmount
) {

  public static BudgetResponse from(Budget budget, Long usedAmount) {
    return new BudgetResponse(
        budget.getId(),
        budget.getUser().getId(),
        budget.getPeriod(),
        budget.getAmount(),
        budget.getStartDate(),
        budget.getEndDate(),
        budget.getCreatedAt(),
        budget.getUpdatedAt(),
        usedAmount,
        budget.getAmount() - usedAmount
    );
  }
}
