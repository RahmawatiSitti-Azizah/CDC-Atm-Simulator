package com.mitrais.cdc.cli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class BeanConfig {

    @Bean
    public Scanner inputScanner() {
        return new Scanner(System.in);
    }
}
