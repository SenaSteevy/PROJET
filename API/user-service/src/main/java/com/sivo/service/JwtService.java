package com.sivo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivo.entity.User;
import com.sivo.repository.UserRepository;
import com.sivo.request.JwtRequest;
import com.sivo.response.JwtResponse;
import com.sivo.util.JwtUtil;

import reactor.core.publisher.Mono;

@Service
public class JwtService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public ResponseEntity<JwtResponse> createJwtToken(JwtRequest jwtRequest) {
		String username = jwtRequest.getUsername();
		String userPassword = jwtRequest.getPassword();
		try {
			User user = authenticate(username, userPassword).block();
			String newGeneratedToken = jwtUtil.generateToken(user);
			return ResponseEntity.ok(new JwtResponse(user, newGeneratedToken));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}

	public Mono<String> getUserNameFromToken(String jwtToken) {
		return Mono.fromCallable(() -> jwtUtil.getUserNameFromtoken(jwtToken));
	}

	public Mono<User> loadUserByUsername(String username) {
		return Mono.fromCallable(() -> userRepository.findByEmail(username)
				.orElseThrow(() -> new Exception("User not found with username: " + username)));
	}

	public Mono<Boolean> validateToken(String jwtToken, String username) {
		return Mono.fromCallable(() -> userRepository.findByEmail(username)).flatMap(userOptional -> userOptional
				.map(user -> jwtUtil.validateToken(jwtToken, user)).orElse(Mono.just(false)));
	}

	private Mono<User> authenticate(String username, String userPassword) {
		return Mono.fromCallable(() -> {
			return userRepository.findByEmail(username)
					.filter(user -> passwordEncoder().matches(userPassword, user.getPassword()))
					.orElseThrow(() -> new Exception("Bad Credentials"));
		});
	}

}
