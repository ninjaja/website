package com.company.website.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration
 *
 * @author Dmitry Matrizaev
 * @since 20.04.2020
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("classpath:static/");
        registry.setOrder(-1);
        registry
                .addResourceHandler("/**/favicon.ico")
                .addResourceLocations("classpath:static/");
        registry.setOrder(-1);
    }


}
