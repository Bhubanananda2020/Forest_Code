package com.forest_code.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.forest_code.config.CustomUserDetails;
import com.forest_code.entity.EmployeeEntity;
import com.forest_code.repo.EmployeeRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployeeRepo employeeRepo;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		EmployeeEntity userentity = this.employeeRepo.getByEusername(username);

		if (userentity == null) {
			throw new UsernameNotFoundException("Username not found, Please enter a valid username");
		}
		return new CustomUserDetails(userentity);
	}

}