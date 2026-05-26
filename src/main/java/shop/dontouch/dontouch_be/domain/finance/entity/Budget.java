package shop.dontouch.dontouch_be.domain.finance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Budget extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BudgetPeriod period;

  @Column(nullable = false)
  private Long amount;

  private LocalDate startDate;

  private LocalDate endDate;

  public void update(BudgetPeriod period, Long amount, LocalDate startDate, LocalDate endDate) {
    if (period != null) {
      this.period = period;
    }
    if (amount != null) {
      this.amount = amount;
    }
    if (period == BudgetPeriod.CUSTOM) {
      this.startDate = startDate;
      this.endDate = endDate;
    } else {
      this.startDate = null;
      this.endDate = null;
    }
  }
}
