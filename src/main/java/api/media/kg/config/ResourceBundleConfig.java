package api.media.kg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Configuration
public class ResourceBundleConfig {
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("en"));
        return messageSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
