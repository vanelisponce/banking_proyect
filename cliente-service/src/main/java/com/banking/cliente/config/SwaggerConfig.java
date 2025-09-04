package com.banking.cliente.config;

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
    public OpenAPI clienteServiceAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Cliente Service Development");
        
        Contact contact = new Contact();
        contact.setEmail("dev@banking.com");
        contact.setName("Banking Development Team");
        contact.setUrl("https://www.banking.com");        
        
        Info info = new Info()
                .title("Cliente Service API")
                .version("1.0")
                .contact(contact)
                .description("API para gestión de clientes del sistema bancario. " +
                           "Permite crear, consultar, actualizar y eliminar clientes.");
                
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}