package com.sivo.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sivo.entity.User;

import reactor.core.publisher.Mono;

@FeignClient(value = "user-service")
public interface UserServiceFeignClient {

    @GetMapping("/loadUserByUsername/{username}")
    Mono<User> loadUserByUsername(@PathVariable("username") String username);

    @GetMapping("/getUserNameFromToken/{jwtToken}")
    Mono<String> getUserNameFromtoken(@PathVariable("jwtToken") String jwtToken);

    @GetMapping("/validateToken/{jwtToken}/{username}")
    Mono<Boolean> validateToken(@PathVariable("jwtToken") String jwtToken, @PathVariable("username") String username);
}
