package com.forest_code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.forest_code.model.User;
import com.forest_code.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {


	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.userRepo.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("No User");
		}
		return new CustomUserDetails(user);
	}

}
