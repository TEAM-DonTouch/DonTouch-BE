package shop.dontouch.dontouch_be.domain.finance.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;

@Getter
@NoArgsConstructor
public class BudgetRequest {

  @NotNull(message = "예산 기간은 필수입니다.")
  private BudgetPeriod period;

  @NotNull(message = "예산 금액은 필수입니다.")
  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  private LocalDate startDate;

  private LocalDate endDate;
}
