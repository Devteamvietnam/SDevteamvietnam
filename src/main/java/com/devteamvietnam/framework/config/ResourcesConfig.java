package com.devteamvietnam.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.framework.interceptor.RepeatSubmitInterceptor;

/**
 * General configuration
 *
 * @author ivan
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** Local file upload path */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**").addResourceLocations("file:" + DevteamConfig.getProfile() + "/");

        /** swagger configuration */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Custom blocking rules
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * Cross-domain configuration
     */
    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Set the access source address
        config.addAllowedOrigin("*");
        // Set access source request header
        config.addAllowedHeader("*");
        // Set access source request method
        config.addAllowedMethod("*");
        // Configure cross-domain settings on the interface
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
