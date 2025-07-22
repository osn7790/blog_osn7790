package com.blog.osn7790._core.config;

import com.blog.osn7790._core.incerceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/board/**", "/user/**", "/reply/**")
                .excludePathPatterns("/board/{id:\\d+}");
    }

}
