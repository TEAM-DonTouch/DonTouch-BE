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
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
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
  public TransactionResponse createTransaction(UUID userId, TransactionRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("createTransaction: 유효하지 않은 userId {}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      log.warn("createTransaction: 탈퇴한 유저의 거래 생성 시도 userId {}", user.getId());
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> {
          log.warn("createTransaction: 유효하지 않은 categoryId {}", request.getCategoryId());
          return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        });
    validateCategoryOwnership(category, userId);

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

  public List<TransactionResponse> getAllTransactionsByCategoryId(UUID userId, UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      log.warn("getAllTransactionsByCategoryId: 유효하지 않은 categoryId {}", categoryId);
      throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    List<Transaction> transactions = transactionRepository.findAllByCategoryIdWithUserIdAndCategory(userId, categoryId);

    return transactions.stream()
        .map(TransactionResponse::from)
        .toList();
  }


  @Transactional
  public TransactionResponse updateTransaction(UUID userId, UUID transactionId, TransactionUpdateRequest request) {

    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("updateTransaction: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });

    if (!transaction.getUser().getId().equals(userId)) {
      log.warn("updateTransaction: 본인 소유가 아닌 거래 수정 시도 userId {} transactionId {}", userId, transactionId);
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    if (transaction.getUser().getStatus() == UserStatus.WITHDRAWN) {
      log.warn("updateTransaction: 탈퇴한 유저의 거래 수정 시도 userId {}, transactionId {}", transaction.getUser().getId(), transaction.getId());
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    Category category = null;
    if (request.getCategoryId() != null) {
      category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> {
            log.warn("updateTransaction: 유효하지 않은 categoryId {}", request.getCategoryId());
            return new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
          });
      validateCategoryOwnership(category, userId);
    }
    transaction.update(category, request.getAmount(), request.getMemo(), request.getType(), request.getTransactionDate());

    Transaction updatedTransaction = transactionRepository.saveAndFlush(transaction);

    return TransactionResponse.from(updatedTransaction);
  }

  @Transactional
  public void deleteTransaction(UUID userId, UUID transactionId) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> {
          log.warn("deleteTransaction: 유효하지 않은 transactionId {}", transactionId);
          return new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        });

    if (!transaction.getUser().getId().equals(userId)) {
      log.warn("deleteTransaction: 본인 소유가 아닌 거래 삭제 시도 userId {} transactionId {}", userId, transactionId);
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    transaction.delete();
  }

  private void validateCategoryOwnership(Category category, UUID userId) {
    if (category.getUser() != null && !category.getUser().getId().equals(userId)) {
      log.warn("validateCategoryOwnership: 본인 소유가 아닌 카테고리 사용 시도 userId {} categoryId {}", userId, category.getId());
      throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
  }

}
