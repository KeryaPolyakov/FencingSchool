package com.kirillpolyakov.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Fencing school System Api",
                description = "API для организации работы школы фехтования, позволяющий обеспечивать запись " +
                        "учеников на тренировки",
                version = "1.0.0",
                contact = @Contact(
                        name = "Polyakov Kirill",
                        email = "kerya_polyakov@icloud.com",
                        url = "https://kirillpolyakov.dev"
                )
        )
)
public class OpenApiConfig {
}
