package com.sivo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sivo.entity.Role;
import com.sivo.entity.User;
import com.sivo.proxy.UserServiceFeignClient;

import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    public Mono<String> getUserNameFromtoken(String jwtToken) {
        return Mono.defer(() -> {
          
                return userServiceFeignClient.getUserNameFromtoken(jwtToken)
                        .doOnSuccess(username -> System.out.println("response: " + username))
                        .onErrorResume(e -> {
                            System.out.println("Exception response: " + e);
                            return Mono.empty();
                        });
            
            });
    }


    public Mono<UserDetails> loadUserByUsername(String username) {
        return userServiceFeignClient.loadUserByUsername(username)
                .map(user -> {
                	System.out.println("user = "+user);
                    return new org.springframework.security.core.userdetails.User(
                            user.getEmail(), user.getPassword(), getAuthorities(user));
                });
    }

    public Mono<Boolean> validateToken(String jwtToken, String username) {
        return userServiceFeignClient.validateToken(jwtToken, username)
                .map(isTokenValid -> {return isTokenValid; });
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Role role = user.getRole();
        role.getPermissionList().stream()
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getDescription())));

        return authorities;
    }
}
