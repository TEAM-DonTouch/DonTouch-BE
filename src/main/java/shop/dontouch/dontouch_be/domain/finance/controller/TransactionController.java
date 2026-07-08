package shop.dontouch.dontouch_be.domain.finance.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionUpdateRequest;
import shop.dontouch.dontouch_be.domain.finance.service.TransactionService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController implements TransactionControllerDocs {

  private final TransactionService transactionService;

  @LogMonitoring
  @PostMapping("/me")
  public ResponseEntity<TransactionResponse> createTransaction(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody TransactionRequest request) {
    TransactionResponse response = transactionService.createTransaction(currentUser.getUserId(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<List<TransactionResponse>> getMyTransactions(
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    List<TransactionResponse> responses = transactionService.getAllTransactionsByUserId(currentUser.getUserId());
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @GetMapping("/me/categories/{category-id}")
  public ResponseEntity<List<TransactionResponse>> getAllTransactionsByCategoryId(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "category-id") UUID categoryId
  ) {
    List<TransactionResponse> responses = transactionService.getAllTransactionsByCategoryId(currentUser.getUserId(), categoryId);
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @PatchMapping("/me/{transaction-id}")
  public ResponseEntity<TransactionResponse> updateTransaction(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "transaction-id") UUID transactionId,
      @Valid @RequestBody TransactionUpdateRequest request
  ) {
    TransactionResponse response = transactionService.updateTransaction(currentUser.getUserId(), transactionId, request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @DeleteMapping("/me/{transaction-id}")
  public ResponseEntity<Void> deleteTransaction(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "transaction-id") UUID transactionId
  ) {
    transactionService.deleteTransaction(currentUser.getUserId(), transactionId);
    return ResponseEntity.noContent().build();
  }

  ///  ADMIN

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
    List<TransactionResponse> responses = transactionService.getAllTransactions();
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/{transaction-id}")
  public ResponseEntity<TransactionResponse> getTransactionByTransactionId(
      @PathVariable(name = "transaction-id") UUID transactionId
  ) {
    TransactionResponse response = transactionService.getTransactionByTransactionId(transactionId);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/users/{user-id}")
  public ResponseEntity<List<TransactionResponse>> getAllTransactionsByUserId(
      @PathVariable(name = "user-id") UUID userId
  ) {
    List<TransactionResponse> responses = transactionService.getAllTransactionsByUserId(userId);
    return ResponseEntity.ok(responses);
  }
}
