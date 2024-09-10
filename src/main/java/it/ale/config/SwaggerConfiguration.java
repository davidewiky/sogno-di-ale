package it.ale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

	@Bean
	  OpenAPI customOpenApi() {
	    return new OpenAPI().info(new Info().title("SOGNO DI ALE API").version("0.0.0")
				.description("Swagger per sviluppo del BE del sito Sogno di Ale"));
	  }
}
