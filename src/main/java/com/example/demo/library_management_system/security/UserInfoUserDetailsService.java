package com.example.demo.library_management_system.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.library_management_system.entity.User;
import com.example.demo.library_management_system.repository.UserRepository;


@Service
public class UserInfoUserDetailsService implements UserDetailsService{

	
	
	
	
	
		@Autowired
		UserRepository userInfoRepository;
		
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User userInfo=userInfoRepository.findByUsername(username).get();
			 if (userInfo == null) {
		            throw new UsernameNotFoundException("User not found: " + username);
		        }
			 
			 return new org.springframework.security.core.userdetails.User(
		                userInfo.getUsername(),
		                userInfo.getPassword(),
		                // Set authorities based on user roles or permissions
		                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		        );
			//return userInfo.map(UserInfoUserDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found"+username));
		}

	 
	}
	
	