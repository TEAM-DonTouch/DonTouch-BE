package shop.dontouch.dontouch_be.domain.finance.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.finance.constant.BudgetPeriod;
import shop.dontouch.dontouch_be.domain.finance.dto.request.BudgetRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.BudgetResponse;
import shop.dontouch.dontouch_be.domain.finance.entity.Budget;
import shop.dontouch.dontouch_be.domain.finance.repository.BudgetRepository;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final UserRepository userRepository;

  @Transactional
  public BudgetResponse saveBudget(UUID userId, BudgetRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("saveBudget: 유효하지 않은 userId {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      log.warn("saveBudget: 탈퇴한 유저의 예산 생성/수정 시도 userId {}", user.getId());
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    if (request.getPeriod() == BudgetPeriod.CUSTOM) {
      if (request.getStartDate() == null || request.getEndDate() == null) {
        log.warn("saveBudget: CUSTOM 기간인데 날짜 누락 userId-{}", user.getId());
        throw new CustomException(ErrorCode.BUDGET_PERIOD_DATE_REQUIRED);
      }
      if (request.getStartDate().isAfter(request.getEndDate())) {
        log.warn("saveBudget: 시작일이 종료일보다 늦음 userId-{}", user.getId());
        throw new CustomException(ErrorCode.BUDGET_PERIOD_DATE_INVALID);
      }
    }

    Budget budget = budgetRepository.findByUser(user)
        .map(existing -> {                    // user가 budget 설정을 했다면
          existing.update(
              request.getPeriod(),
              request.getAmount(),
              request.getStartDate(),
              request.getEndDate());
          return existing;
        })
        .orElseGet(() -> budgetRepository.save(     // user가 처음 budget 설정한다면
            Budget.builder()
                .user(user)
                .period(request.getPeriod())
                .amount(request.getAmount())
                .startDate(request.getPeriod() == BudgetPeriod.CUSTOM ? request.getStartDate() : null)
                .endDate(request.getPeriod() == BudgetPeriod.CUSTOM ? request.getEndDate() : null)
                .build()
        ));

    return BudgetResponse.from(budget);
  }

  public List<BudgetResponse> getAllBudgets() {
    return budgetRepository.findAll().stream()
        .map(BudgetResponse::from)
        .toList();
  }

  public BudgetResponse getBudgetByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("getBudgetByUserId: 유효하지 않은 userId {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Budget budget = budgetRepository.findByUser(user)
        .orElseThrow(() -> {
          log.warn("getBudgetByUserId: budget 없음 userId {}", userId);
          return new CustomException(ErrorCode.BUDGET_NOT_FOUND);
        });
    return BudgetResponse.from(budget);
  }

  @Transactional
  public void deleteBudget(UUID userId, UUID budgetId) {
    Budget budget = budgetRepository.findById(budgetId)
        .orElseThrow(() -> {
          log.warn("deleteBudget: 유효하지 않은 budgetId {}", budgetId);
          return new CustomException(ErrorCode.BUDGET_NOT_FOUND);
        });

    if (!budget.getUser().getId().equals(userId)) {
      log.warn("deleteBudget: 본인 소유가 아닌 예산 삭제 시도 userId {} budgetId {}", userId, budgetId);
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    budgetRepository.delete(budget);
  }

}
