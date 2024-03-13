package de.dhbw2go.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationOpenAPI {

    @Value("${dhbw2go.openapi.development.url}")
    private String developmentURL;

    @Value("${dhbw2go.openapi.production.url}")
    private String productionURL;

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl(developmentURL);
        devServer.setDescription("Server URL for Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(productionURL);
        prodServer.setDescription("Server URL for Production environment");

        Contact contact = new Contact();
        contact.setName("DHBW2go");
        contact.setEmail("info@dhbw2go.de");
        contact.setUrl("https://www.dhbw2go.de/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("DHBW2go API")
                .version("1.0")
                .termsOfService("https://www.dhbw2go.de/terms")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
