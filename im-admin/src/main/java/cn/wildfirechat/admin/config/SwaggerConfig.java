package cn.wildfirechat.admin.config;

import cn.wildfirechat.admin.common.annotation.SwaggerSpec;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * swagger配置
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {

    private final String[] tradeApiArray = {"com.zodiac.api.controller.trade"};
    private final String[] defaultExceptPackage = {"org.springframework.boot.autoconfigure.web.servlet"};

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    @Bean
    public Docket createDefaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(SwaggerConfig.handlerSpecDefaultAPI(defaultExceptPackage)).build();
    }

//    @Bean
//    public Docket createTradeApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .groupName("交易平台")
//                .securitySchemes(Collections.singletonList(apiKey()))
//                .securityContexts(Collections.singletonList(securityContext()))
//                .select()
////                .apis(RequestHandlerSelectors.basePackage("com.zodiac.api.controller.trade"))
////                .paths(PathSelectors.any())
//                .apis(SwaggerConfig.handlerSpecAPI("交易平台")).build();
//    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("IMChat后台API文件").description("客制化IMChat API文件").version("1.0.0").build();
    }

    /**
     * Predicate that matches RequestHandler with given base package name for the class of the handler method. This
     * predicate includes all request handlers matching the provided basePackage
     *
     * @param basePackage - base package of the classes
     * @return this
     */
    public static Predicate<RequestHandler> defaultPackage(String[]... basePackage) {
        return input -> declaringClass(input).map(handlerDefaultPackage(basePackage)).orElse(true);
    }

    /**
     * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
     *
     * @param basePackage 扫描包路径
     * @return Function
     */
    private static Function<Class<?>, Boolean> handlerDefaultPackage(final String[]... basePackage) {
        List<String> result = new ArrayList<>();
        Arrays.stream(basePackage).flatMap(Stream::of).forEach(result::add);
        return input -> {
            for (String strPackage : result) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    private static Optional<Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

    public static Predicate<RequestHandler> handlerSpecDefaultAPI(String[]... basePackage) {
        return input -> {
            List<String> result = new ArrayList<>();
            Arrays.stream(basePackage).flatMap(Stream::of).forEach(result::add);
            for (String strPackage : result) {
                if (input.declaringClass().getName().startsWith(strPackage)) {
                    return false;
                }
            }
            return handlerApi(input, "", true);
        };
    }

    public static Predicate<RequestHandler> handlerSpecAPI(String specName) {
        return input -> handlerApi(input, specName, false);
    }

    private static Boolean handlerApi(RequestHandler requestHandler, String specName, boolean isDefault) {
        Class<?> clazz = requestHandler.declaringClass();

        boolean isShow = false;

        Optional<SwaggerSpec> swaggerSpecOptional =
                requestHandler.findAnnotation(SwaggerSpec.class);
        if (clazz.isAnnotationPresent(SwaggerSpec.class) && swaggerSpecOptional.isPresent()) {
            if (!isDefault) {
                if (clazz.getAnnotation(SwaggerSpec.class).value().equals(specName)
                        && swaggerSpecOptional.get().value().equals(specName)) {
                    isShow = true;
                } else if (swaggerSpecOptional.get().value().equals(specName)) {
                    isShow = true;
                }
            }
        } else if (clazz.isAnnotationPresent(SwaggerSpec.class) && !swaggerSpecOptional.isPresent()) {
            if (!isDefault) {
                if (clazz.getAnnotation(SwaggerSpec.class).value().equals(specName)) {
                    isShow = true;
                }
            }
        } else if (swaggerSpecOptional.isPresent()) {
            if (!isDefault) {
                if (swaggerSpecOptional.get().value().equals(specName)) {
                    isShow = true;
                }
            }

        } else {
            if (isDefault) {
                isShow = true;
            }
        }
        return isShow;
    }

    private SecurityContext securityContext() {

        return SecurityContext.builder().securityReferences(defaultAuth()).build();

    }

    private List<SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];

        authorizationScopes[0] = authorizationScope;

        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
//    }

}
