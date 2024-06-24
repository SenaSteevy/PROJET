package com.sivo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.sivo.proxy.UserServiceFeignClient;
import com.sivo.service.UserService;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfig {

	 @Autowired
	    JwtAuthenticationEntryPoint authenticationEntryPoint;

	    @Autowired
	    UserService userService;

	    @Autowired
	    UserServiceFeignClient userServiceFeignClient;

	    @Bean
	    public TokenAuthenticationFilter tokenAuthenticationFilter() {
	        return new TokenAuthenticationFilter(userService);
	    }

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http.csrf().disable().authorizeExchange()
				.pathMatchers("user-service/authenticate", "user-service/newRegisterRequest", "loadUserByUsername/**",
						"/getUserNameFromToken/**", "validateToken/**")
				.permitAll()
				.pathMatchers(org.springframework.http.HttpHeaders.ALLOW).permitAll()
				.anyExchange().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.addFilterBefore(tokenAuthenticationFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ReactiveUserDetailsService userDetailsService() {
		return username -> {
			// Construct a UserDetails object based on userDto
			// Adjust this according to the actual structure of your user details
			UserDetails userDetails = userService.loadUserByUsername(username).block();

			return Mono.just(userDetails);
		};
	}

}
