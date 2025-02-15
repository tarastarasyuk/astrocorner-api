package com.itzroma.astrocornerapi;

import com.itzroma.astrocornerapi.security.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AstrocornerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AstrocornerApiApplication.class, args);
    }

}
