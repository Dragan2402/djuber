package com.djuber.djuberbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Djuber", version = "1.0", description = "Djuber application APIs"))
@EnableJpaRepositories
public class DjuberBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DjuberBackendApplication.class, args);
    }

}
