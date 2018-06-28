package com.aabanegas.techtest.configuration;

import com.aabanegas.techtest.service.dto.TransactionDTO;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Retrieves all registered microservices Swagger resources.
 */
@Component
@Primary
@Profile("swagger")
public class PaymentsSwaggerConfiguration {

    @Autowired
    private TypeResolver typeResolver;

    /**
     * Provides generic defaults and convenience methods for Springfox/Swagger framework configuration.
     */
    @Bean
    public Docket api() {
        ResolvedType payments = typeResolver.resolve(TransactionDTO.class);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(Lists.newArrayList(new ApiKey("authorization", "authorization", "header")))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aabanegas.techtest"))
                .paths(PathSelectors.any()).build()
                .pathMapping("/")
                .protocols(Sets.newHashSet("http", "https"))
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages());
//                .additionalModels(typeResolver.resolve(payments));
    }

    /**
     * @return Swagger object with API info
     */
    private ApiInfo apiInfo() {
        return new ApiInfo("Payments API", "API to retrieve the documents related with Application.",
                "v1", "Terms of service", new Contact("Aurelio Aragones",
                "https://www.linkedin.com/in/aurelio-aragon%C3%A9s-banegas-a6016340/",
                "aurelio.aragones@gmail.com"), "", "", null);
    }

    /**
     * @return Set of generic response messages that override the default/global response messages
     */
    private List<ResponseMessage> responseMessages() {
        return Lists.newArrayList(new ResponseMessageBuilder()
                        .code(400)
                        .message("An input validation error occurred. One or more input fields are invalid.").build(),
                new ResponseMessageBuilder()
                        .code(401)
                        .message("Unauthorized - JWT validation failed").build(),
                new ResponseMessageBuilder()
                        .code(404)
                        .message("Not found").build(),
                new ResponseMessageBuilder()
                        .code(503)
                        .message("The service is unavailable").build());
    }
//    private final RouteLocator routeLocator;
//
//    public PaymentsSwaggerConfiguration(RouteLocator routeLocator) {
//        this.routeLocator = routeLocator;
//    }
//
//    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//
//        //Add the default swagger resource that correspond to the gateway's own swagger doc
//        resources.add(swaggerResource("default", "/v2/api-docs"));
//
//        //Add the registered microservices swagger docs as additional swagger resources
//        List<Route> routes = routeLocator.getRoutes();
//        routes.forEach(route -> {
//            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs")));
//        });
//
//        return resources;
//    }
//
//    private SwaggerResource swaggerResource(String name, String location) {
//        SwaggerResource swaggerResource = new SwaggerResource();
//        swaggerResource.setName(name);
//        swaggerResource.setLocation(location);
//        swaggerResource.setSwaggerVersion("2.0");
//        return swaggerResource;
//    }
}
