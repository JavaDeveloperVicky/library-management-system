package com.example.demo.library_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.library_management_system.entity.AuthRequest;
import com.example.demo.library_management_system.entity.User;
import com.example.demo.library_management_system.jwt.JwtService;
import com.example.demo.library_management_system.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtTokenUtil;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public User login(String username, String password) {
		// ... authentication logic
		return userRepository.findByUsername(username).get();
	}

	public AuthRequest createUser(User userDTO) {
		// Validate user input
		// Check if the username already exists
		if (userRepository.existsByUsername(userDTO.getUsername())) {
			throw new RuntimeException("Username already exists.");
		}

		// Encode the password before saving the user
		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);

		User savedUser = userRepository.save(userDTO);

		return new AuthRequest(savedUser.getUsername());
	}

}
