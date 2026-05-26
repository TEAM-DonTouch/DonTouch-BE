package shop.dontouch.dontouch_be.domain.finance.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

  private final BudgetService budgetService;

  @PutMapping
  public ResponseEntity<BudgetResponse> saveBudget(
      @Valid @RequestBody BudgetRequest request
  ) {
    BudgetResponse response = budgetService.saveBudget(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<BudgetResponse>> getAllBudgets() {
    List<BudgetResponse> responses = budgetService.getAllBudgets();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/users/{user-id}")
  public ResponseEntity<BudgetResponse> getBudgetByUserId(
      @PathVariable(name = "user-id") UUID userId
  ) {
    BudgetResponse response = budgetService.getBudgetByUserId(userId);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{budget-id}")
  public ResponseEntity<Void> deleteBudget(
      @PathVariable(name = "budget-id") UUID budgetId
  ){
    budgetService.deleteBudget(budgetId);
    return ResponseEntity.noContent().build();
  }

}
