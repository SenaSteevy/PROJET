package com.sivo.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.sivo.service.UserService;
import com.sivo.util.JwtUtil;

import reactor.core.publisher.Mono;

@Configuration
public class TokenAuthenticationFilter implements WebFilter {

    private final UserService userService;

    @Autowired
    JwtUtil jwtUtil;
    
    private static final Log log = LogFactory.getLog(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        if (!path.contains("authenticate") && !path.contains("newRegisterRequest")) {
            final String header = request.getHeaders().getFirst("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String jwtToken = header.substring(7);

                return userService.getUserNameFromtoken(jwtToken)
                        .flatMap(username -> {
                            log.info("username: "+ username);

                            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                                return userService.loadUserByUsername(username)
                                        .flatMap(userDetails -> jwtUtil.validateToken(jwtToken, userDetails.getUsername())
                                                .flatMap(validated -> {
                                                    if (validated) {
                                                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                                        usernamePasswordAuthenticationToken.setDetails(request.getRemoteAddress());
                                                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                                        return chain.filter(exchange);
                                                    } else {
                                                        // Handle invalid token
                                                        return Mono.error(new Exception("Invalid token"));
                                                    }
                                                })
                                        );
                            } else {
                                // Handle invalid username
                                return Mono.error(new Exception("Invalid username"));
                            }
                        })
                        .onErrorResume(e -> {
                            // Handle errors, e.g., token expired
                            log.error(e.getMessage());
                            return Mono.error(new Exception("JWT Token expired"));
                        });

            } else {
                System.out.println("JWT doesn't start with Bearer or is Empty");
            }
        }
        return chain.filter(exchange);
    }

}
