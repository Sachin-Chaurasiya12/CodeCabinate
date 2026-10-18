package com.example.ConnectService.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    public OpenAPI openapi(){
        return new OpenAPI().info(
            new Info()
            .description("This is a Chat app service.")
            .title("Connect Service")
            
        );
    }
}
