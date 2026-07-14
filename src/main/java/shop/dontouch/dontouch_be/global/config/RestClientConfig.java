package shop.dontouch.dontouch_be.global.config;

import java.time.Duration;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder()
      .requestFactory(clientHttpRequestFactory());
  }

  @Bean
  public RestOperations restOperations() {
    return new RestTemplate(clientHttpRequestFactory());
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() {
    HttpClientSettings settings = HttpClientSettings.defaults()
      .withTimeouts(Duration.ofSeconds(3), Duration.ofSeconds(5));

    return ClientHttpRequestFactoryBuilder
      .detect()
      .build(settings);
  }
}