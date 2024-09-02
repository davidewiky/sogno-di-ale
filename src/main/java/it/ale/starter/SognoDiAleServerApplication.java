package it.ale.starter;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = "it.ale")
@EnableJpaRepositories(basePackages = "it.ale.repository")
@EntityScan(basePackages = "it.ale.model")
@OpenAPIDefinition(info = @Info(title = "IlSognoDiAle API", version = "1.0", description = "SognoDiAle"))
@ServletComponentScan
public class SognoDiAleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SognoDiAleServerApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
	  return new ModelMapper();
	}
}
