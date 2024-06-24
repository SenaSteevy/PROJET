package com.sivo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.sivo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class JwtUtil {

	private static  final String SECRET_KEY = "sena_guiffo_steevy";
	
	private static final int TOKEN_VALIDITY = 3600 * 5;
	public Mono<String> getUserNameFromtoken(String token) {
		return Mono.just(getClaimFromToken(token, Claims::getSubject));
		
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
		
	}
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	
	public Mono<Boolean> validateToken(String token, String  username) {
	   return getUserNameFromtoken(token)
	    		.map( usernameToken -> usernameToken.equals(username) && !isTokenExpired(token));
	   
	}

	
	private Boolean isTokenExpired(String token) {
		final Date expirationDate = getExpirationDateFromToken(token);
		return expirationDate.before(new Date());
	}
	
	private Date getExpirationDateFromToken(String token) {
		
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(user.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY *1000))
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512,SECRET_KEY)
				.compact(); 
		
	}
}
