package com.aki.websocket;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by AKi on 2018/9/27.
 * ws类注入的配置
 * ws最大发送字数的配置
 * 映射网页配置
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebSocketConfig implements WebMvcConfigurer, ServletContextInitializer {
    /**
     * ws类注入
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * ws最大发送字数
     * @Configuration 这个注解针对这个方法
     * @ComponentScan 这个注解针对这个方法
     * @EnableAutoConfiguration 这个注解针对这个方法
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("org.apache.tomcat.websocket.textBufferSize");
        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","1024000");
    }

    /**
     * 直接访问页面的简单写法
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //首页
        registry.addViewController("sketchpad").setViewName("thymeleaf/sketchpad");
    }
}