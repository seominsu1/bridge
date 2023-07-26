package bridge.demo.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openApi() {
		Info info = new Info()
			.title("[Bridge] API Documents")
			.version("v0.0.1")
			.description("[Bridge] 프로젝트의 API 명세서입니다.");
		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
