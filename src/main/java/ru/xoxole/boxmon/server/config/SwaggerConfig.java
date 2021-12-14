package ru.xoxole.boxmon.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;


@Configuration
public class SwaggerConfig {

    private static final String SERVICE_TITLE = "boxmon-server";
    private static final String SERVICE_DESCRIPTION = "";
    private static final String EMPTY_STRING = "";
    private static final String BASE_PACKAGE_NAME = "ru.xoxole.boxmon.server";



    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo(SERVICE_TITLE,
                        SERVICE_DESCRIPTION,
                        EMPTY_STRING,
                        EMPTY_STRING,
                        new Contact("","",""),
                        EMPTY_STRING,
                        EMPTY_STRING,
                        Collections.emptyList()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE_NAME))
                .paths(PathSelectors.any())
                .build();
    }
}