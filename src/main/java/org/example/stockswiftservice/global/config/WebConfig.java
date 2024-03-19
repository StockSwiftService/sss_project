package org.example.stockswiftservice.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://frontapp-7i8iek5ur-jhseos-projects.vercel.app") // 프론트엔드의 배포 도메인으로 변경
                .allowedMethods("*")
                .allowCredentials(true);
    }
}