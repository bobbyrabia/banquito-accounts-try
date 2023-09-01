package ec.edu.espe.banquito.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Cors {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://banquito-bancaweb.web.app/")
                        .allowedOrigins("https://arquitectura-1sa89r3l.uc.gateway.dev/")
                        .allowedOrigins("https://localhost:4200/")
                        .allowedOrigins("https://localhost:4200/client")
                        .exposedHeaders("Content-Disposition")
                        .allowedMethods("*");
            }
        };
    }
}
