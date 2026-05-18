package shop.dontouch.dontouch_be.domain.finance.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionUpdateRequest;
import shop.dontouch.dontouch_be.domain.finance.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController implements TransactionControllerDocs {

  private final TransactionService transactionService;

  @PostMapping
  public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
    TransactionResponse response = transactionService.createTransaction(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // TODO 추후 ADMIN 권한 접근으로 변경 예정
  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
    List<TransactionResponse> responses = transactionService.getAllTransactions();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/users/{user-id}")
  public ResponseEntity<List<TransactionResponse>> getAllTransactionByUserId(
      @PathVariable(name = "user-id") UUID userId
  ) {
    List<TransactionResponse> responses = transactionService.getAllTransactionsByUserId(userId);
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{transaction-id}")
  public ResponseEntity<TransactionResponse> getTransactionByTransactionId(
      @PathVariable(name = "transaction-id") UUID transactionId
  ) {
    TransactionResponse response = transactionService.getTransactionByTransactionId(transactionId);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{transaction-id}")
  public ResponseEntity<TransactionResponse> updateTransaction(
      @PathVariable(name = "transaction-id") UUID transactionId,
      @Valid @RequestBody TransactionUpdateRequest request
  ) {
    TransactionResponse response = transactionService.updateTransaction(transactionId, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{transaction-id}")
  public ResponseEntity<Void> deleteTransaction(
      @PathVariable(name = "transaction-id") UUID transactionId
  ) {
    transactionService.deleteTransaction(transactionId);
    return ResponseEntity.noContent().build();
  }
}
