package com.xiaozhuzhijia.webbbs.web.config;

import com.xiaozhuzhijia.webbbs.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 将自定义拦截器注册到spring
     * 使@Autowired注解生效
     * @return
     */
    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getLoginInterceptor())
                //过滤所有
                .addPathPatterns("/**")
                //添加排除项
                .excludePathPatterns(
                        "/favicon.ico",
                        "/images/**",
                        "/js/**",
                        "/css/**");
        ;
    }

    /**
     * 解决资源跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

    /**
     * 解决等静态资源访问不到问题
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
