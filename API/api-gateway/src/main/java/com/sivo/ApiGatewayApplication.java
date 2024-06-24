package com.sivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.sivo.proxy") 
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	 @Bean
	    public Encoder feignEncoder() {
	        return new JacksonEncoder();
	    }

	    @Bean
	    public Decoder feignDecoder() {
	        return new JacksonDecoder();
	    }
	@Bean
    public Feign.Builder feignBuilder(Encoder encoder, Decoder decoder) {
        return Feign.builder().encoder(encoder).decoder(decoder);
    }
}
