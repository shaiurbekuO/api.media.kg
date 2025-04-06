package api.media.kg.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // general info
        Info info = new Info()
                .title("Media.kg API")
                .version("1.0.0")
                .description("Below is the API documentation for the Media.kg project.")
                .contact(new Contact()
                        .name("Omurbek")
                        .email("shaiurbekuuluomurbek@gmail.com")
                        .url("https://t.me/omurbekSh")
                )
                .license(new License()
                        .name("Student.kg")
                        .url("https://student.kg/")
                )
                .termsOfService("Question and answer group: https://t.me/code_kg_group");

        // servers (ishlatiladigan serverlar)
        Server server1 = new Server()
                .description("Local")
                .url("http://localhost:8080");

        Server server2 = new Server()
                .description("DEV")
                .url("http://api.media.kg");

        Server server3 = new Server()
                .description("PROD")
                .url("http://api.media.kg");

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");

        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setName("bearerAuth");
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.bearerFormat("JWT");
        securityScheme.setIn(SecurityScheme.In.HEADER);
        securityScheme.setScheme("bearer");

        Components components = new Components();
        components.addSecuritySchemes("bearerAuth", securityScheme);

        // collect all together
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setServers(List.of(server1, server2, server3));
        openAPI.setSecurity(List.of(securityRequirement));
        openAPI.components(components);

        // return-xe
        return openAPI;
    }
}