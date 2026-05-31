package shop.dontouch.dontouch_be.global.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shop.dontouch.dontouch_be.domain.ai.dto.SpendingAlertRequest;
import shop.dontouch.dontouch_be.domain.ai.dto.SpendingAlertResponse;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class DontouchAiClient {

    private final RestClient dontouchAiRestClient;

    public SpendingAlertResponse requestSpendingAlert(SpendingAlertRequest request) {
        try {
            SpendingAlertResponse response = dontouchAiRestClient.post()
                .uri("/api/ai/alerts/spending")
                .body(request)
                .retrieve()
                .body(SpendingAlertResponse.class);

            if (response == null) {
                throw new CustomException(ErrorCode.AI_RESPONSE_FAILED);
            }
            return response;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}
