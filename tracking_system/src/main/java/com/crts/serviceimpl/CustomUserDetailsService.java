package com.crts.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crts.config.CustomUserDetails;
import com.crts.entity.UserEntity;
import com.crts.repo.UserRepo;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = this.userRepo.getUserEntityByUserNameEmail(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException("Username not found, Please enter a valid username");
		}
		return new CustomUserDetails(userEntity);
	}

}