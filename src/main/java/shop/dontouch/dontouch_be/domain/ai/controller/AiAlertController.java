package shop.dontouch.dontouch_be.domain.ai.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.ai.dto.SpendingAlertResponse;
import shop.dontouch.dontouch_be.domain.ai.service.AiAlertService;

@RestController
@RequestMapping("/api/ai/alerts")
@RequiredArgsConstructor
public class AiAlertController {

    private final AiAlertService aiAlertService;

    @PostMapping("/spending/{userId}")
    public ResponseEntity<SpendingAlertResponse> generateSpendingAlert(@PathVariable UUID userId) {
        return ResponseEntity.ok(aiAlertService.generateSpendingAlert(userId));
    }
}
