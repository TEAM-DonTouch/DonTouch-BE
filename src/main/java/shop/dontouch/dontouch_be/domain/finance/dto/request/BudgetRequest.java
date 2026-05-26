package shop.dontouch.dontouch_be.domain.finance.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;

@Getter
@NoArgsConstructor
public class BudgetRequest {

  @NotNull
  private UUID userId;

  @NotNull
  private BudgetPeriod period;

  @NotNull
  @Min(value = 1, message = "금액은 1 이상이어야 합니다.")
  private Long amount;

  private LocalDate startDate;

  private LocalDate endDate;
}
