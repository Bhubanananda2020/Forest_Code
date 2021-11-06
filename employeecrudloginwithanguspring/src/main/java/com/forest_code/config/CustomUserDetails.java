package com.forest_code.config;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.forest_code.entity.EmployeeEntity;

public class CustomUserDetails implements UserDetails {

	private EmployeeEntity employeeEntity;

	public CustomUserDetails(EmployeeEntity employeeEntity) {
		this.employeeEntity = employeeEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		HashSet<SimpleGrantedAuthority> set = new HashSet<>();
		set.add(new SimpleGrantedAuthority(this.employeeEntity.getEroll()));
		return set;
	}

	@Override
	public String getPassword() {
		return this.employeeEntity.getEpassword();
	}

	@Override
	public String getUsername() {
		return this.employeeEntity.getEusername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.employeeEntity.isEnabled();
	}

}
