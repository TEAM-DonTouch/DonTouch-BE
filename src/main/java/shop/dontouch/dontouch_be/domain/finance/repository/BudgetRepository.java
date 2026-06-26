package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dontouch.dontouch_be.domain.finance.entity.Budget;
import shop.dontouch.dontouch_be.domain.user.entity.User;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

  Optional<Budget> findByUser(User user);
}
