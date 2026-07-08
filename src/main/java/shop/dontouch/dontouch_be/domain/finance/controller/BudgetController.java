package shop.dontouch.dontouch_be.domain.finance.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.finance.dto.request.BudgetRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.BudgetResponse;
import shop.dontouch.dontouch_be.domain.finance.service.BudgetService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController implements BudgetControllerDocs {

  private final BudgetService budgetService;

  @LogMonitoring
  @PutMapping("/me")
  public ResponseEntity<BudgetResponse> saveBudget(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody BudgetRequest request
  ) {
    BudgetResponse response = budgetService.saveBudget(currentUser.getUserId(), request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<BudgetResponse> getMyBudget(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    BudgetResponse response = budgetService.getBudgetByUserId(currentUser.getUserId());
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @DeleteMapping("/me/{budget-id}")
  public ResponseEntity<Void> deleteBudget(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "budget-id") UUID budgetId
  ) {
    budgetService.deleteBudget(currentUser.getUserId(), budgetId);
    return ResponseEntity.noContent().build();
  }

  ///  ADMIN

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping
  public ResponseEntity<List<BudgetResponse>> getAllBudgets() {
    List<BudgetResponse> responses = budgetService.getAllBudgets();
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/users/{user-id}")
  public ResponseEntity<BudgetResponse> getBudgetByUserId(
      @PathVariable(name = "user-id") UUID userId
  ) {
    BudgetResponse response = budgetService.getBudgetByUserId(userId);
    return ResponseEntity.ok(response);
  }

}
