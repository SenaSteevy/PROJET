package com.sivo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sivo.entity.User;
import com.sivo.repository.UserRepository;
import com.sivo.request.JwtRequest;
import com.sivo.response.JwtResponse;
import com.sivo.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;

	
	private AuthenticationManager authenticationManager;
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
				this.authenticationManager = authenticationManager;
	}

	public ResponseEntity<?> createJwtToken(JwtRequest jwtRequest) throws Exception {
		String username = jwtRequest.getUsername();
		String userPassword = jwtRequest.getPassword();
		authenticate(username, userPassword);

		final UserDetails userdetails = loadUserByUsername(username);

		String newGeneratedToken = jwtUtil.generateToken(userdetails);
		
		User user = userRepository.findById(username).get();
		
		return  ResponseEntity.ok( new JwtResponse(user,newGeneratedToken));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
	}


	private Set  getAuthorities(User user) {
		Set authorities =  new HashSet();
		
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+ role.getRoleName()));
		});
		
		return authorities;
		
	}
	private void authenticate(String username, String userPassword) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userPassword));
		} catch (DisabledException e) {
			throw new Exception("User is disabled");
		} catch (BadCredentialsException e) {
			throw new Exception("Bad Credentials from user");
		}
	}
}
