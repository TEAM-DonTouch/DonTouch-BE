package shop.dontouch.dontouch_be.domain.finance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.dontouch.dontouch_be.domain.finance.entity.Budget;
import shop.dontouch.dontouch_be.domain.user.entity.User;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

  Optional<Budget> findByUser(User user);

  @Query("SELECT b FROM Budget b JOIN FETCH b.user")
  List<Budget> findAllWithUser();
}
