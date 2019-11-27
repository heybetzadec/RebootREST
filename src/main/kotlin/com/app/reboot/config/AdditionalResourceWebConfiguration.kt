package com.app.reboot.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class AdditionalResourceWebConfiguration: WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/media/**").addResourceLocations("file:media/")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD").allowCredentials(true)
//        registry.addMapping("/**").allowedOrigins("http://localhost:8082").allowedMethods("GET", "POST","PUT", "DELETE")
//        registry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:8080")
    }

//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//                .allowedMethods("*")
//    }

}