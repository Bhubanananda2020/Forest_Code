package com.crts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crts.entity.DeptEntity;
import com.crts.entity.UserDeptEntity;


@Service
public interface UserDeptService {

	/* ========== Save list of Department user role ================ */
	public UserDeptEntity saveUserDeptAccess(List<UserDeptEntity> ude,int userid);

	/* ========== Delete list of Department user role ================ */
	public int DeleteUserDeptAccess(int uId);
	
	
	
	
	public List<Object[]> getAllUserByDeptid(int deptid);
	
	public String IsUserAdmin(int userid);
	
	/* ========= No of Sub Department===============*/
	public int noOfDepartment (int uid);
	
	/* ========= No of User in Same Department===============*/
	public int noOfUserInDepartment(int uid);
	
	
	public List<DeptEntity> AllAdminDepartment(int uid);
	public List<String> AllSupAdminDepartment();
	
	public List<UserDeptEntity> getAllRollByUserId(int userid);
	
	
	
	/* ============ GET ALL DEPARTMENT ============ */
	public String getAllDeptpermission(int uid, int deptid);
	

}
