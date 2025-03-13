package api.media.kg.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Бардык /api/ эндпоинттери үчүн
                .allowedOrigins("http://localhost:63342") // Фронтенд домени
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Уруксат берилген методдор
                .allowedHeaders("*") // Бардык заголовокторго уруксат
                .allowCredentials(true); // Кукилерди колдонууга уруксат
    }
}
