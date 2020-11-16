package com.devteamvietnam.admin.core.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devteamvietnam.common.config.DevteamConfig;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 interface configuration
 *
 * @author ivan
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    /** System basic configuration */
    @Autowired
    private DevteamConfig ruoyiConfig;

    /** Whether to open swagger */
    @Value("${swagger.enabled}")
    private boolean enabled;

    /** Set the unified prefix of the request */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    /**
     * Create API
     */
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                // Whether to enable Swagger
                .enable(enabled)
                // The basic information used to create the API, displayed in the document page (custom display information)
                .apiInfo(apiInfo())
                // Set which interfaces are exposed to Swagger display
                .select()
                // Scan all annotated apis, this way is more flexible
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // Scan the swagger annotations in the specified package
                // .apis(RequestHandlerSelectors.basePackage("com.ruoyi.project.tool.swagger"))
                // Scan all .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                /* Set the security mode, swagger can set the access token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(pathMapping);
    }

    /**
     * Security mode, here specifies that the token is passed through the Authorization header request header
     */
    private List<ApiKey> securitySchemes()
    {
        List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    /**
     * Security context
     */
    private List<SecurityContext> securityContexts()
    {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    /**
     * The default security quote
     */
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * Add summary information
     */
    private ApiInfo apiInfo()
    {
        // Customize with ApiInfoBuilder
        return new ApiInfoBuilder()
                // set title
                .title("Title: If According to Management System_Interface Document")
                // description
                .description ("Description: used to manage the personnel information of the group companies, including XXX, XXX modules...")
                // author information
                .contact(new Contact(ruoyiConfig.getName(), null, null))
                // version
                .version("version number:" + ruoyiConfig.getVersion())
                .build();
    }
}
