package shop.dontouch.dontouch_be.domain.finance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category")
  List<Transaction> findAllWithCategory();

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.user.id = :userId")
  List<Transaction> findAllByUserIdWithCategory(@Param("userId") UUID userId);

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.user.id = :userId AND t.category.id = :categoryId")
  List<Transaction> findAllByCategoryIdWithUserIdAndCategory(@Param("userId") UUID userId, @Param("categoryId") UUID categoryId);

  boolean existsByCategoryId(UUID categoryId);

  @Query("""
      SELECT COALESCE(SUM(t.amount), 0)
      FROM Transaction t
      WHERE t.user.id = :userId
        AND t.type = :type
        AND t.transactionDate >= :start
        AND t.transactionDate < :endExclusive
      """)
  Long sumAmountByUserIdAndTypeAndDateRange(
      @Param("userId") UUID userId,
      @Param("type") TransactionType type,
      @Param("start") LocalDateTime start,
      @Param("endExclusive") LocalDateTime endExclusive
  );
}
