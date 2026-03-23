package com.padel.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI padelOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Padel Service API")
                .description("API REST - Gestion de terrains de padel")
                .version("1.0.0")
                .contact(new Contact().name("Padel Service").email("admin@padel.be")))
            .servers(List.of(new Server().url("http://localhost:8080/api").description("Dev local")));
    }
}
