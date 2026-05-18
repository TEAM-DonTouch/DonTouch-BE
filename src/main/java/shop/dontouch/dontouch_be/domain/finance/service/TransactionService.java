package shop.dontouch.dontouch_be.domain.finance.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionUpdateRequest;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;
import shop.dontouch.dontouch_be.domain.finance.repository.TransactionRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;

  @Transactional
  public TransactionResponse createTransaction(TransactionRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> {
          log.warn("createTransaction: 유효하지 않은 userId {}", request.getUserId());
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Transaction transaction = transactionRepository.save(
        Transaction.builder()
            .user(user)
            .amount(request.getAmount())
            .type(request.getType())
            .memo(request.getMemo())
            .transactionDate(request.getTransactionDate())
            .build()
    );
    return TransactionResponse.from(transaction);
  }

  @Transactional(readOnly = true)
  public List<TransactionResponse> getAllTransactions() {
    return transactionRepository.findAll().stream()
        .map(TransactionResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TransactionResponse> getAllTransactionsByUserId(UUID userId) {
    if(!userRepository.existsById(userId)) {
      log.warn("getAllTransactionsByUserId: 유효하지 않은 userId {}", userId);
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

    return transactions.stream()
        .map(TransactionResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public TransactionResponse getTransactionByTransactionId(UUID transactionId) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("getTransactionByTransactionId: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });
    return TransactionResponse.from(transaction);
  }

  @Transactional
  public TransactionResponse updateTransaction(UUID transactionId, TransactionUpdateRequest request) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("updateTransaction: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });
    transaction.update(request.getAmount(), request.getMemo(), request.getType(), request.getTransactionDate());
    return TransactionResponse.from(transaction);
  }

  @Transactional
  public void deleteTransaction(UUID transactionId) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("deleteTransaction: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });
    transaction.delete();
  }

}
