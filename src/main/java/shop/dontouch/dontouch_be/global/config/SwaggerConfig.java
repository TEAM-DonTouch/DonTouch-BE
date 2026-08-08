package shop.dontouch.dontouch_be.global.config;

import shop.dontouch.dontouch_be.global.properties.SpringDocProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "🍱 DonTouch 🍱",
        description = """
            ### 🌐 DonTouch 웹사이트 🌐 : https://www.dontouch.shop
            [**웹사이트 바로가기**](https://www.dontouch.shop)
            
            ### 💻 **GitHub 저장소**
            - **[백엔드 소스코드](https://github.com/TEAM-DonTouch/DonTouch-BE)**
              백엔드 개발에 관심이 있다면 저장소를 방문해보세요.

            ### ⚠️ 공통 응답 코드

            아래 오류는 특정 엔드포인트가 아니라 **요청 자체가 잘못됐을 때** 발생하므로, 각 API 문서에는 따로 표기하지 않습니다.
            모든 오류 응답의 본문 형식은 `{"errorCode": "...", "errorMessage": "..."}` 로 동일합니다.

            - `RESOURCE_NOT_FOUND` (404 NOT_FOUND): 존재하지 않는 경로입니다. 요청 URL을 확인해주세요.
            - `METHOD_NOT_ALLOWED` (405 METHOD_NOT_ALLOWED): 해당 경로가 지원하지 않는 HTTP 메서드입니다. 응답의 `Allow` 헤더에서 지원 메서드를 확인할 수 있습니다.
            - `UNSUPPORTED_MEDIA_TYPE` (415 UNSUPPORTED_MEDIA_TYPE): 지원하지 않는 Content-Type입니다. 요청 본문이 있는 API는 `application/json`으로 보내주세요.
            - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버 내부 오류입니다. 재현 방법과 함께 백엔드에 알려주세요.
            """,
        version = "0.0.1v"
    )
)
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SpringDocProperties.class)
public class SwaggerConfig {

  private final SpringDocProperties properties;

  @Bean
  public OpenAPI openAPI() {
    SecurityScheme apiKey = new SecurityScheme()
        .type(Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(In.HEADER)
        .name("Authorization");

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
        .addSecurityItem(new SecurityRequirement().addList("Bearer Token"));
  }

  @Bean
  public OpenApiCustomizer serverCustomizer() {
    return openApi -> {
      properties.servers().forEach(server ->
          openApi.addServersItem(new io.swagger.v3.oas.models.servers.Server()
              .url(server.url())
              .description(server.description()))
      );
    };
  }
}

