package de.dhbw2go.backend.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${dhbw2go.openapi.development.url}")
    private String developmentURL;

    @Value("${dhbw2go.openapi.production.url}")
    private String productionURL;

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setName("DHBW2go");
        contact.setEmail("info@dhbw2go.de");
        contact.setUrl("https://www.dhbw2go.de/");

        final License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        final Info info = new Info()
                .title("DHBW2go API")
                .version("1.0")
                .termsOfService("https://www.dhbw2go.de/terms")
                .contact(contact)
                .license(mitLicense);

        final Server devServer = new Server();
        devServer.setUrl(developmentURL);
        devServer.setDescription("Server URL for Development environment");

        final Server prodServer = new Server();
        prodServer.setUrl(productionURL);
        prodServer.setDescription("Server URL for Production environment");

        final Components components = new Components()
                .addSecuritySchemes("Authentication with JWT", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .scheme("Bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .components(components)
                .security(List.of(new SecurityRequirement().addList("Authentication with JWT")));
    }
}
