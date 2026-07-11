package shop.dontouch.dontouch_be.global.config;

import java.time.Duration;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient.Builder restClientBuilder() {
    HttpClientSettings settings = HttpClientSettings.defaults()
      .withTimeouts(Duration.ofSeconds(3), Duration.ofSeconds(5));

    ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder
      .detect()
      .build(settings);

    return RestClient.builder()
      .requestFactory(requestFactory);
  }
}