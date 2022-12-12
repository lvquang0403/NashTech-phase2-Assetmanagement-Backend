package com.nashtech.rookies.java05.AssetManagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
    @Component
    @Profile("prd")
    @OpenAPIDefinition(servers = @Server(url = "https://rookie06assetmanagement.azurewebsites.net"))
    public static class PrdOpenAPIDefinitionConfiguration {
    }

    @Component
    @Profile("local")
    @OpenAPIDefinition(servers = @Server(url = "http://localhost:8080"))
    public static class LocalOpenAPIDefinitionConfiguration {
    }
}
