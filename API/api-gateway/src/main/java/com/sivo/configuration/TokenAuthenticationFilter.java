package com.sivo.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.sivo.service.CustomUserDetailsService;
import com.sivo.util.JwtUtil;

import reactor.core.publisher.Mono;

@Configuration
public class TokenAuthenticationFilter implements WebFilter {

    private final CustomUserDetailsService userService;
    private final JwtUtil jwtUtil;

    private static final Log log = LogFactory.getLog(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(CustomUserDetailsService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        if (!path.contains("authenticate") && !path.contains("newRegisterRequest")) {
            final String header = request.getHeaders().getFirst("Authorization");
            
//            request.getHeaders().forEach((key, value) -> {
//                log.debug("Header '" + key + "' = " + String.join(",", value));
//            });
            
            log.debug("request : "+path);
            log.debug("token : "+header);

            if (header != null && header.startsWith("Bearer ")) {
                String jwtToken = header.substring(7);

                try {
                    String username = userService.getUserNameFromToken(jwtToken);

                    if (username != null ) {
                        UserDetails userDetails = userService.loadUserByUsername(username);

                        if (jwtUtil.validateToken(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authToken.setDetails(request.getRemoteAddress());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            log.debug("User authenticated and token valid");
                        } else {
                            return onError(exchange, "Unauthorized: Invalid token");
                        }
                    } else {
                        log.debug("Username is null");
                        return onError(exchange, "Unauthorized: Invalid username or authentication context");
                    }
                } catch (Exception e) {
                    return onError(exchange, "Internal Server Error: Error processing JWT token");
                }
            } else {
                return onError(exchange, "Unauthorized: JWT is empty or malformed");
            }
        }
        
  
        // Allow request to continue down the filter chain
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        log.debug(errorMessage);
        return exchange.getResponse().setComplete();
    }
}
