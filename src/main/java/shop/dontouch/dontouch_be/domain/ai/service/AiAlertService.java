package shop.dontouch.dontouch_be.domain.ai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.ai.dto.SpendingAlertRequest;
import shop.dontouch.dontouch_be.domain.ai.dto.SpendingAlertResponse;
import shop.dontouch.dontouch_be.domain.finance.constant.TransactionType;
import shop.dontouch.dontouch_be.domain.finance.entity.Transaction;
import shop.dontouch.dontouch_be.domain.finance.repository.TransactionRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.client.DontouchAiClient;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiAlertService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final DontouchAiClient dontouchAiClient;

    public SpendingAlertResponse generateSpendingAlert(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        long totalExpense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .filter(t -> t.getTransactionDate().isAfter(startOfMonth))
            .mapToLong(Transaction::getAmount)
            .sum();

        String spendingSummary = buildSpendingSummary(user.getNickname(), totalExpense, transactions, startOfMonth);

        SpendingAlertRequest request = new SpendingAlertRequest(userId, spendingSummary);
        return dontouchAiClient.requestSpendingAlert(request);
    }

    private String buildSpendingSummary(String nickname, long totalExpense, List<Transaction> transactions, LocalDateTime startOfMonth) {
        StringBuilder sb = new StringBuilder();
        sb.append("사용자 닉네임: ").append(nickname).append("\n");
        sb.append("이번 달 총 지출: ").append(String.format("%,d", totalExpense)).append("원\n");
        sb.append("지출 내역:\n");

        transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .filter(t -> t.getTransactionDate().isAfter(startOfMonth))
            .forEach(t -> sb.append("- ").append(t.getMemo())
                .append(": ").append(String.format("%,d", t.getAmount())).append("원\n"));

        return sb.toString();
    }
}
