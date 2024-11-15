package com.example.demo.library_management_system.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI()
         .info(new Info().title("Book JWT Authentication"))
         .addSecurityItem(new SecurityRequirement().addList("SecurityScheme"))
         .components(new Components().addSecuritySchemes("SecurityScheme", new SecurityScheme()
	     .name("SecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

		
	}
	
	
}
