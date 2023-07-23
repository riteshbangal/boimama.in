package in.boimama.readstories.config.swagger;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${app.openapi.dev-url}")
    private String devUrl;

    @Value("${app.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {

        final Contact contact = new Contact();
        contact.setEmail("boimama@gmail.com");
        contact.setName("Boimama");
        contact.setUrl("https://www.boimama.in");

        final License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        final Info info = new Info()
                .title("Open API for Boimama.in")
                .version("0.0.1")
                .contact(contact)
                .description("API documentation for Boimama.in").termsOfService("https://www.boimama.in")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(getDevServer(), getProdServer()));
    }

    private Server getDevServer() {
        return getServer(devUrl, "Server URL for Development environment");
    }

    private Server getProdServer() {
        return getServer(prodUrl, "Server URL for Production environment");
    }

    private Server getServer(String pServerUrl, String pDescription) {
        final Server server = new Server();
        server.setUrl(pServerUrl);
        server.setDescription(pDescription);
        return server;
    }
}
