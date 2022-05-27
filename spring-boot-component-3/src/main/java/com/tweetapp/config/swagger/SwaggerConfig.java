package com.tweetapp.config.swagger;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

//import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String SWAGGER_VERSION = "1.0";
    private static final String LICENCE = "Licence";
    private static final String TITLE = "Tweet Application";
    private static final String DESCRIPTION = "This is a tweet application solution ";
    private static final String EMAIL = "iqbalbcis@gmail.com";

    @Bean
    public Docket swaggerConfiguration() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(postPaths()::apply)
                .build().apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));

    }

	private Predicate<String> postPaths() {
		return (regex("/api/v1.0/tweets.*"));
	}

    //===if have multiple path
//	private Predicate<String> postPaths() {
//		return or(regex("/lawfirm.*"), regex("/authenticate"), regex("/refresh"));
//	}

    private ApiKey apiKey() {
        return new ApiKey("jwtToken", "Authorization", "header");
    }
    //===============================================================

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .version(SWAGGER_VERSION)
                .description(DESCRIPTION)
                .license(LICENCE)
                .contact(new Contact("", "", EMAIL)) //name, url, email
                .build();

    }
}