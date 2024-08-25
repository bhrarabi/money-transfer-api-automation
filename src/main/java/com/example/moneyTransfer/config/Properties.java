package com.example.moneyTransfer.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bazarpay")
@Data
public class Properties {
    private String baseUrl;
    private String firstUsername;
    private String firstPassword;
    private String secondUsername;
    private String secondPassword;
}
