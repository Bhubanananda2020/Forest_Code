package com.crts.dto;

import java.util.List;

import com.crts.entity.UserEntity;

public class UserDTO {
	
	
	private UserEntity userEntity;

	private List<UserEntity> lstuserEntity;

	private String role;
	private String message;
	public UserEntity getUserEntity() {
		return userEntity;
	}
	public String getMessage() {
		return message;
	}
	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<UserEntity> getLstuserEntity() {
		return lstuserEntity;
	}
	public void setLstuserEntity(List<UserEntity> lstuserEntity) {
		this.lstuserEntity = lstuserEntity;
	}

	
	
	

}
