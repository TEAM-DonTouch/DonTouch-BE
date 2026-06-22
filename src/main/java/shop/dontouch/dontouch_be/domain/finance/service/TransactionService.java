package shop.dontouch.dontouch_be.domain.finance.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionUpdateRequest;
import shop.dontouch.dontouch_be.domain.finance.entity.Category;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;
import shop.dontouch.dontouch_be.domain.finance.repository.CategoryRepository;
import shop.dontouch.dontouch_be.domain.finance.repository.TransactionRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  @Transactional
  public TransactionResponse createTransaction(TransactionRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> {
          log.warn("createTransaction: 유효하지 않은 userId {}", request.getUserId());
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> {
          log.warn("createTransaction: 유효하지 않은 categoryId {}", request.getCategoryId());
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });

    Transaction transaction = transactionRepository.save(
        Transaction.builder()
            .user(user)
            .category(category)
            .amount(request.getAmount())
            .type(request.getType())
            .memo(request.getMemo())
            .transactionDate(request.getTransactionDate())
            .build()
    );
    return TransactionResponse.from(transaction);
  }

  public List<TransactionResponse> getAllTransactions() {
    return transactionRepository.findAllWithCategory().stream()
        .map(TransactionResponse::from)
        .toList();
  }

  public TransactionResponse getTransactionByTransactionId(UUID transactionId) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("getTransactionByTransactionId: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });
    return TransactionResponse.from(transaction);
  }

  public List<TransactionResponse> getAllTransactionsByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      log.warn("getAllTransactionsByUserId: 유효하지 않은 userId {}", userId);
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    List<Transaction> transactions = transactionRepository.findAllByUserIdWithCategory(userId);

    return transactions.stream()
        .map(TransactionResponse::from)
        .toList();
  }

  public List<TransactionResponse> getAllTransactionsByCategoryId(UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      log.warn("getAllTransactionsByCategoryId: 유효하지 않은 categoryId {}", categoryId);
      throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    List<Transaction> transactions = transactionRepository.findAllByCategoryIdWithCategory(categoryId);

    return transactions.stream()
        .map(TransactionResponse::from)
        .toList();
  }


  @Transactional
  public TransactionResponse updateTransaction(UUID transactionId, TransactionUpdateRequest request) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("updateTransaction: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });

    Category category = null;
    if (request.getCategoryId() != null) {
      category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> {
            log.warn("updateTransaction: 유효하지 않은 categoryId {}", request.getCategoryId());
            return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
          });
    }
    transaction.update(category, request.getAmount(), request.getMemo(), request.getType(), request.getTransactionDate());
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
