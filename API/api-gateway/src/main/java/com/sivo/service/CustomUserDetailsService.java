package com.sivo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivo.entity.Permission;
import com.sivo.entity.Role;
import com.sivo.entity.User;
import com.sivo.repository.UserRepository;
import com.sivo.request.AuthRequest;
import com.sivo.response.AuthResponse;
import com.sivo.util.JwtUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Autowired
    JwtUtil jwtUtil;
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<GrantedAuthority> authorities = buildAuthorities(user.getRole());
 
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> buildAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

        for (Permission permission : role.getPermissionList()) {
            authorities.add(new SimpleGrantedAuthority(permission.getDescription()));
        }
        
        return authorities;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    // Method to extract username from JWT token
    public String getUserNameFromToken(String jwtToken) {
        try {
           return jwtUtil.extractUsername(jwtToken);
        } catch (Exception e) {
            // Handle invalid JWT or other exceptions
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

	public ResponseEntity<AuthResponse> authenticate(AuthRequest authRequest) {

		Optional<User> optionalUser = userRepository.findByEmail(authRequest.getUsername());
    	if(optionalUser.isPresent() && passwordEncoder().matches(authRequest.getPassword(),optionalUser.get().getPassword())) {
    		
    		try {
    			
    			UserDetails userDetails = loadUserByUsername(authRequest.getUsername());
    			String jwtToken = jwtUtil.generateToken(userDetails);
    			AuthResponse response = new AuthResponse(optionalUser.get(),jwtToken );
    			
    			return ResponseEntity.ok(response);
    		}catch(Exception e) {
    			return ResponseEntity.notFound().build();
    		}
    	}
    	else {
    		return ResponseEntity.notFound().build();
    	}
	}
	
}
