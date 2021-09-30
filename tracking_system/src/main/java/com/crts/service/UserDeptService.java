package com.crts.service;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.crts.entity.UserDeptEntity;
import com.crts.entity.UserEntity;


@Service
public interface UserDeptService {

	/* ========== Save list of Department user role ================ */
	public UserDeptEntity saveUserDeptAccess(List<UserDeptEntity> ude);

	/* ========== Delete list of Department user role ================ */
	public int DeleteUserDeptAccess(int uId);
	
	
	
	
	public List<Object[]> getAllUserByDeptid(int deptid);
	
	public String IsUserAdmin(int userid);
	
	/* ========= No of Sub Department===============*/
	public int noOfDepartment (int uid);
	
	/* ========= No of User in Same Department===============*/
	public int noOfUserInDepartment(int uid);
	
	
	public List<String> AllAdminDepartment(int uid);
	
	public List<UserDeptEntity> getAllRollByUserId(int userid);
	
	
	
	/* ============ GET ALL DEPARTMENT ============ */
	public String getAllDeptpermission(int uid, int deptid);
	

}
