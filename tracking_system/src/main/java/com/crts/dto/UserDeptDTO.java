package com.crts.dto;

import com.crts.entity.UserDeptEntity;

public class UserDeptDTO {

	private UserDeptEntity userDeptEntity;
	private String message;
	public UserDeptEntity getUserDeptEntity() {
		return userDeptEntity;
	}
	public String getMessage() {
		return message;
	}
	public void setUserDeptEntity(UserDeptEntity userDeptEntity) {
		this.userDeptEntity = userDeptEntity;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	

}
