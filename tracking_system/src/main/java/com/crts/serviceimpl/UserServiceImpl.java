package com.crts.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.crts.entity.UserEntity;
import com.crts.repo.UserRepo;
import com.crts.service.UserService;

@Service
public class UserServiceImpl extends java.lang.Object implements UserService {

	@Autowired
	private UserRepo userRepo;


	
	//BcryptEncoding
	public String bcryptEncoding(String  originalPassword) {
		String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);
		return generatedSecuredPasswordHash;
	}
	
	//BcryptDeCoding
	public boolean bcryptDecoding(String originalPassword, String hashedPassword) {
		boolean matched = BCrypt.checkpw(originalPassword, hashedPassword);
		return matched;
	}

	
	/* ===== Validate User By username or email and password ===== */
	public UserEntity userValidate(String userName, String password) {
		UserEntity validuser = null;
		try {
			UserEntity entity = this.userRepo.getUserEntityByUserNameEmail(userName);
			System.out.println();
			System.out.println();
			
			System.out.println(entity);
			System.out.println();
			System.out.println();
			System.out.println();
			boolean bcryptDecoding = bcryptDecoding(password, entity.getuPassword());
			if ((userName.equals(entity.getuName()) || userName.equals(entity.getuEmail()))
					&& bcryptDecoding && entity.isuIsActive()) {
				validuser = entity;
			} else {
				validuser = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validuser;
	}


	
	/* ===== Validate User By username or email ===== */
	public UserEntity validatingUserNameOrEmailid(String username) {
		UserEntity userEntity = null;
		try {
			userEntity = this.userRepo.getUserEntityByUserNameEmail(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userEntity;
	}

	/* ===== Update User Password ===== */
	public boolean updatePassword(UserEntity ue) {
		UserEntity userEntity = null;
		boolean flag = false;
		try {
			
			
			String bcryptEncodingPass = bcryptEncoding(ue.getuPassword());
			ue.setuPassword(bcryptEncodingPass);
			userEntity = this.userRepo.saveAndFlush(ue);
			if (userEntity != null) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/* ===== View All User ===== */	
	public List<UserEntity> getAllUser() {
		return this.userRepo.findAll();
	}

	/* ===== View All User FOR ADMIN VIEW ===== */	
	public List<UserEntity> getAllUserByuIdForAdminView(int uid) {
		return this.userRepo.getAllUserByuIdForAdminView(uid);
	}

	
	
	
	public String getEmailIdByFirstName(String username) {
		return this.userRepo.getEmailIdByFirstName(username);
	}

	
	
	
	
	
	
	


	
	
	/* ===== Create New User ===== */
	public UserEntity CreateNewUser(UserEntity ue) {	
		
		System.out.println("pass----->" + ue.getuPassword());
		String encpass = bcryptEncoding(ue.getuPassword());
		System.out.println("---ss--->"+encpass);
		ue.setuPassword(encpass);
	
		
		return this.userRepo.saveAndFlush(ue);
	}
	

	public UserEntity getById(int uid) {
		return this.userRepo.getById(uid);
	}


}