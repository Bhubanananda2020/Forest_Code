package com.crts.config;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.crts.entity.UserEntity;



public class CustomUserDetails implements UserDetails {

	private UserEntity userEntity;

	public CustomUserDetails(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		HashSet<SimpleGrantedAuthority> set = new HashSet<>();
		return set;
	}

	@Override
	public String getPassword() {
		return this.userEntity.getuPassword();
	}

	@Override
	public String getUsername() {
		return this.userEntity.getuName();
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
		return this.userEntity.isuIsActive();
	}

}
