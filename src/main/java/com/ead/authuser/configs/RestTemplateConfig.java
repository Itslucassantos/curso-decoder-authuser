package com.ead.authuser.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // para fazer o balanceamento de carga
    @LoadBalanced
    // Essa configuração permite que você injete um RestTemplate em outras partes do seu código, facilitando a
    // comunicação com serviços externos por meio de requisições HTTP.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
