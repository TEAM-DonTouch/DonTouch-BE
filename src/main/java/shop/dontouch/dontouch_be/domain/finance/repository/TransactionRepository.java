package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  List<Transaction> findAllByUserId(UUID userId);
}
