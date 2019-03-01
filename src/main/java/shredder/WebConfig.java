package shredder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

//    @Value("${ALLOWED_ORIGINS}")
//    private String frontEnd;

    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry){
//        String frontEnd = env.getProperty("ALLOWED_ORIGINS");
        registry.addMapping("/**")
//                .allowedOrigins(origins.split(","))
//                .allowedOrigins("*")
//                .allowedOrigins(frontEnd.split(","))
                .allowedOrigins("https://shredder-react.herokuapp.com","http://localhost:3000")
                .allowedMethods("PUT", "DELETE", "POST", "OPTIONS", "HEAD", "GET")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

}
