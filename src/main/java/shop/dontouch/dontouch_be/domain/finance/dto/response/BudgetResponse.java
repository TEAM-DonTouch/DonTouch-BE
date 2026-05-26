package shop.dontouch.dontouch_be.domain.finance.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;
import shop.dontouch.dontouch_be.domain.finance.entity.Budget;

@Getter
@Builder
public class BudgetResponse {

  private UUID budgetId;
  private UUID userId;
  private BudgetPeriod period;
  private Long amount;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static BudgetResponse from(Budget budget) {
    return BudgetResponse.builder()
        .budgetId(budget.getId())
        .userId(budget.getUser().getId())
        .period(budget.getPeriod())
        .amount(budget.getAmount())
        .startDate(budget.getStartDate())
        .endDate(budget.getEndDate())
        .createdAt(budget.getCreatedAt())
        .updatedAt(budget.getUpdatedAt())
        .build();
  }

}
