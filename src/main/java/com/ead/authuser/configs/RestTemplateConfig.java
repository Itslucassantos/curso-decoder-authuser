package com.ead.authuser.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    // Variável estática com um tempo definido de 5000 milissigundos de TIMEOUT.
    static final int TIMEOUT = 5000;

    // para fazer o balanceamento de carga
    @LoadBalanced
    // Essa configuração permite que você injete um RestTemplate em outras partes do seu código, facilitando a
    // comunicação com serviços externos por meio de requisições HTTP.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT))
                .build();
    }

}
