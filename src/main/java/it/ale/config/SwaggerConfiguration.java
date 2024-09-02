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
	    return new OpenAPI().info(new Info().title("EOCNET DMZ v2 API").version("0.0.0").description(
	        "See docs on Confluence - <a href=\"https://confluence.eoc.ch\" target=\"_blank\">https://confluence.eoc.ch/</a>")
	        .termsOfService("https://www.eoc.ch/").contact(new Contact().name("Team Pizzo di Claro")));
	  }
}
