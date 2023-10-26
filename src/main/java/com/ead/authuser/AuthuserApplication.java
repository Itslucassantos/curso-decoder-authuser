package com.ead.authuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
// para dizer que é um client do eureka server
@EnableEurekaClient
public class AuthuserApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthuserApplication.class, args);
	}

}
