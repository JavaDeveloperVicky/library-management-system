package com.example.demo.library_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.library_management_system.entity.User;
import com.example.demo.library_management_system.jwt.JwtService;
import com.example.demo.library_management_system.repository.BorrowedBookRepository;
import com.example.demo.library_management_system.repository.UserRepository;
import com.example.demo.library_management_system.service.BookService;
import com.example.demo.library_management_system.service.UserService;



@RestController
@RequestMapping("/api/users")
public class AuthController {

	@Autowired
	JwtService jwtService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BorrowedBookRepository borrowedBookRepository;

	@Autowired
	BookService bookService;

	@Autowired
	UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public String generateJWTToken(@RequestParam("username") String username,
	@RequestParam("password") String password) {
	Authentication authentication = authenticationManager.authenticate(
	new UsernamePasswordAuthenticationToken(username,password));
	SecurityContextHolder.getContext().setAuthentication(authentication);
	if (authentication.isAuthenticated()) {
	// return jwtService.generateToken(username);
	String token = jwtService.generateToken(username);
	        System.out.println("Generated Token: " + token); // Log it for debugging
	        return token;
	 
	} else {
	 
	throw new UsernameNotFoundException("Invalid user request!!!");
	 
	}
	}
}

	/*
	 * 
	 * 
	 @PostMapping("/login") public String login(@RequestBody User user) { return
	  JwtService.generateToken(user.getUsername()); }
	 
	 * @PostMapping("/authenticate") public String
	 * generateJWTToken(@RequestParam("username") String username,
	 * 
	 * @RequestParam("password") String password) {
	 * 
	 * Authentication authentication=authManager.authenticate(new
	 * UsernamePasswordAuthenticationToken(username,password));
	 * 
	 * if(authentication.isAuthenticated()) {
	 * 
	 * return jwtService.generateToken(username);
	 * 
	 * } else {
	 * 
	 * throw new UsernameNotFoundException("Invalid user request!!!");
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username,@RequestParam String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
            String token = jwtService.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    */

