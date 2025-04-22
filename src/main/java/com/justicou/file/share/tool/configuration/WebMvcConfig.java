package com.justicou.file.share.tool.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserValidationInterceptor userValidationInterceptor;
    private final CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;

    public WebMvcConfig(
            UserValidationInterceptor userValidationInterceptor,
            CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver
    ) {
        this.userValidationInterceptor = userValidationInterceptor;
        this.currentUserMethodArgumentResolver = currentUserMethodArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userValidationInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserMethodArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*");
    }
}
