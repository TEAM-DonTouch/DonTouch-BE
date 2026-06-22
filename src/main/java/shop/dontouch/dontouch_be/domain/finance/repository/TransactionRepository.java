package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category")
  List<Transaction> findAllWithCategory();

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.user.id = :userId")
  List<Transaction> findAllByUserIdWithCategory(@Param("userId") UUID userId);

  @Query("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.category.id = :categoryId")
  List<Transaction> findAllByCategoryIdWithCategory(@Param("categoryId") UUID categoryId);
}
