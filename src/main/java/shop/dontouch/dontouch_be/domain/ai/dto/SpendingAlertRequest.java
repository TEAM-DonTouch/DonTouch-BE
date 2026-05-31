package shop.dontouch.dontouch_be.domain.ai.dto;

import java.util.UUID;

public record SpendingAlertRequest(
    UUID userId,
    String spendingSummary
) {
}
