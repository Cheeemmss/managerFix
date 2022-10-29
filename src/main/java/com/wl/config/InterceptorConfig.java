package com.wl.config;


import com.wl.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Deprecated
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/login","/register","/file/**","/checkRepeat");
//    }
//
//    @Bean
//    public JWTInterceptor jwtInterceptor(){
//        return new JWTInterceptor();
//    }
}
