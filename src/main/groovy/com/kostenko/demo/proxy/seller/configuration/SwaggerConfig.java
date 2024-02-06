package com.kostenko.demo.proxy.seller.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi(
            @Value("${application-description}") String appDescription,
            @Value("${application-version}") String appVersion
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo sns version")
                        .version(appVersion)
                        .description(appDescription)
                        .contact(new Contact()
                                .email("dima.kostenk@gmail.com")
                                .name("Dmitrij")
                                .url("https://www.linkedin.com/in/дмитрий-костенко-403aa8207/")));
    }
}