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
                        .allowedOrigins("https://striped-reserve-397605.web.app","https://banquito-backoffice.web.app","https://banquito-web.web.app","https://banquito-tellerapp.web.app","http://localhost:4200","https://banquito-bancaweb-v2.web.app")
                        .exposedHeaders("Content-Disposition","Content-Type")
                        .allowedMethods("*");
            }
        };
    }
}
