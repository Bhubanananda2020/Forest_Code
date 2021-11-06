package com.forest_code.jwtautho.springwithjwtautho.services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username.equalsIgnoreCase("forestcode")) {
			return new User("forestcode", "Alpha", new ArrayList<>());
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

}
