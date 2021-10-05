package com.crts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crts.entity.UserEntity;

@Service
public interface UserService {

	/* ===== Validate User By username or email and password ===== */
	public UserEntity userValidate(String userName, String password);

	/* ===== Validate User By username or email ===== */
	public UserEntity validatingUserNameOrEmailid(String username);

	/* ===== Update User Password ===== */
	public boolean updatePassword(UserEntity ue);
	
	/* ===== View All User ===== */	
	public List<UserEntity> getAllUser();

	/* ===== View All User FOR ADMIN VIEW ===== */	
	public List<UserEntity> getAllUserByuIdForAdminView(int uid);

	/* ===== Get Email Id By Firstname ===== */
	public String getEmailIdByFirstName(String username);

	/* ===== Create New User ===== */
	public UserEntity CreateNewUser(UserEntity ue);
	
	public UserEntity getById(int uid);
	
	
	
	
}