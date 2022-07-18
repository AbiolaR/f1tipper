package com.keplerworks.f1tipper.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.view.InternalResourceView
import org.springframework.web.servlet.view.InternalResourceViewResolver


@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {







    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins(
            "http://localhost:4200",
            "http://localhost:4000",
            "http://192.168.2.143:4200",
            "http://media-server.casacam.net:4200",
            "https://app.f1tipper.de",
            "https://media-server.casacam.net:4200",
            "http://tinympc:4200",
            "http://tinympc:4000"
        )
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("index.html")
        registry.addViewController("/home").setViewName("index.html")
    }

    @Bean
    fun internalResourceViewResolver(): ViewResolver? {
        val viewResolver = InternalResourceViewResolver()
        viewResolver.setViewClass(InternalResourceView::class.java)
        return viewResolver
    }

}
