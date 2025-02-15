package com.brunob.notification_service.infrastructure.security.config;

import com.brunob.notification_service.infrastructure.security.annotation.PublicRoute;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class PublicRouteScanner {

    @Setter
    @Getter
    private Set<String> publicRoutes = new HashSet<>();

    @Setter
    @Getter
    private Set<String> csrfIgnoredRoutes = new HashSet<>();

    @Autowired
    public PublicRouteScanner(ApplicationContext applicationContext) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((mappingInfo, handlerMethod) -> {
            PublicRoute methodAnnotation = handlerMethod.getMethodAnnotation(PublicRoute.class);
            PublicRoute classAnnotation = handlerMethod.getBeanType().getAnnotation(PublicRoute.class);

            if (methodAnnotation != null || classAnnotation != null) {
                mappingInfo.getPatternValues().forEach(pattern -> {
                    publicRoutes.add(pattern);
                    if ((methodAnnotation != null && methodAnnotation.ignoreCsrf()) ||
                            (classAnnotation != null && classAnnotation.ignoreCsrf())) {
                        csrfIgnoredRoutes.add(pattern);
                    }
                });
            }
        });

        addSwaggerAndH2Routes();
    }

    private void addSwaggerAndH2Routes() {
        Set<String> swaggerRoutes = Set.of("/swagger-ui/**", "/v3/api-docs/**");
        Set<String> h2Routes = Set.of("/h2-console/**");

        publicRoutes.addAll(swaggerRoutes);
        publicRoutes.addAll(h2Routes);
        csrfIgnoredRoutes.addAll(h2Routes);
    }

}