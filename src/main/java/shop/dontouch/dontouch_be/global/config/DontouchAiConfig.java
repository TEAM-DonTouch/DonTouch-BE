package shop.dontouch.dontouch_be.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DontouchAiConfig {

    @Value("${dontouch-ai.base-url}")
    private String baseUrl;

    @Bean
    public RestClient dontouchAiRestClient() {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
