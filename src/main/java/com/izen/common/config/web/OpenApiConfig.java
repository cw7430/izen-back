package com.izen.common.config.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;

@Configuration
@Profile("!production")
public class OpenApiConfig {

    private final String secretApiKey;

    public OpenApiConfig(@Value("${security.api-key}") String secretApiKey) {
        this.secretApiKey = secretApiKey;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme accessTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .description("Access Token을 입력하세요 (Bearer 제외하고 입력)");

        SecurityScheme refreshTokenScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .description("Refresh Token을 입력하세요 (Bearer 제외하고 입력)");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("access-token", accessTokenScheme)
                        .addSecuritySchemes("refresh-token", refreshTokenScheme));
    }

    @Bean
    public OpenApiCustomizer globalHeaderCustomizer() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().values().forEach(pathItem ->
                        pathItem.readOperations().forEach(operation -> {
                            operation.addParametersItem(
                                    new HeaderParameter()
                                            .name("X-API-Key")
                                            .description("자동 입력된 API Key")
                                            .required(true)
                                            .schema(new StringSchema()._default(secretApiKey))
                            );
                        })
                );
            }
        };
    }
}
