package com.keplerworks.f1tipper.config

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.view.InternalResourceView
import org.springframework.web.servlet.view.InternalResourceViewResolver


@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    @Value("\${server.http.port:80}")
    private val httpPort = 0

    @Value("\${server.port:443}") //@Value("\${server.port:80}")
    private val httpsPort = 0


    @Profile("prod")
    @Bean
    fun servletContainer(): ServletWebServerFactory? {
        val tomcat: TomcatServletWebServerFactory?

        tomcat = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context) {
                val securityConstraint = SecurityConstraint()
                securityConstraint.userConstraint = "CONFIDENTIAL"
                val collection = SecurityCollection()
                collection.addPattern("/*")
                securityConstraint.addCollection(collection)
                context.addConstraint(securityConstraint)
            }
        }
        tomcat.addAdditionalTomcatConnectors(redirectConnector())
        return tomcat
    }

    private fun redirectConnector(): Connector {
        val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.scheme = "http"
        connector.port = httpPort
        connector.secure = false
        connector.redirectPort = httpsPort
        return connector
    }




    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins(
            "http://localhost:4200",
            "http://192.168.2.143:4200",
            "http://media-server.casacam.net:4200",
            "https://media-server.casacam.net:4200",
            "http://tinympc:4200"
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
