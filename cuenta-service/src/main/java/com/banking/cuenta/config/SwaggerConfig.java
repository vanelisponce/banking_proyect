package com.banking.cuenta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI cuentaServiceAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8082");
        devServer.setDescription("Cuenta Service Development");
        
        Contact contact = new Contact();
        contact.setEmail("dev@banking.com");
        contact.setName("Banking Development Team");
        
        Info info = new Info()
                .title("Cuenta Service API")
                .version("1.0")
                .contact(contact)
                .description("API para gestión de cuentas, movimientos y reportes del sistema bancario");
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}