package com.crts.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crts.entity.DeptEntity;
import com.crts.entity.ResponseDept;
import com.crts.entity.UserDeptEntity;
import com.crts.repo.UserDeptRepo;
import com.crts.service.UserDeptService;

@Service
public class UserDeptServiceImpl implements UserDeptService {

	@Autowired
	private UserDeptRepo userDeptRepo;

	/* ========== Save list of Department user role ================ */
	public UserDeptEntity saveUserDeptAccess(List<UserDeptEntity> ude,int userid) {
		UserDeptEntity entity = null;

		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);

		for (UserDeptEntity u1 : ude) {
			u1.setDecreatedby(userid);
			u1.setCreateddate(date);
			entity = this.userDeptRepo.saveAndFlush(u1);
		}
		return entity;
	}

	/* ========== Delete list of Department user role ================ */
	public int DeleteUserDeptAccess(int uId) {
		System.out.println("112aa" + uId);
		int res = 0;

		try {
			res = this.userDeptRepo.DeleteUserDeptAccess(uId);
			System.out.println(res);
		} catch (Exception e) {
			res = 0;
			e.printStackTrace();
		}

		return res;
	}

	public List<Object[]> getAllUserByDeptid(int deptid) {
		return this.userDeptRepo.getAllUserByDeptid(deptid);
	}

	public String IsUserAdmin(int userid) {
		List<String> isUserAdmin = this.userDeptRepo.IsUserAdmin(userid);
		String getreturnerole = "user";
		for (String isuser : isUserAdmin) {
			if (isuser.equalsIgnoreCase("admin")) {
				getreturnerole = "admin";
			} else if (isuser.equalsIgnoreCase("supadmin")) {
				getreturnerole = "supadmin";
			}

		}
		return getreturnerole;
	}

	public List<DeptEntity> AllAdminDepartment(int uid) {
		List<DeptEntity> lstdept = new ArrayList<DeptEntity>();
		List<Object[]> allAdminDepartment = this.userDeptRepo.AllAdminDepartment(uid);
		for (Object[] ob : allAdminDepartment) {
			DeptEntity dd = new DeptEntity();
			dd.setDecode(ob[1].toString());
			dd.setDename(ob[2].toString());
			lstdept.add(dd);
		}
		return lstdept;
	}

	public List<String> AllSupAdminDepartment() {
		return this.userDeptRepo.AllSupAdminDepartment();
	}

	/* ========= No of Sub Department=============== */
	public int noOfDepartment(int uid) {
		return this.userDeptRepo.noOfDepartment(uid);
	}

	/* ========= No of User in Same Department=============== */
	public int noOfUserInDepartment(int uid) {
		return this.userDeptRepo.noOfUserInDepartment(uid);
	}

	@Override
	public List<UserDeptEntity> getAllRollByUserId(int userid) {
		return this.userDeptRepo.getAllRollByUserId(userid);
	}

	public String getAllDeptpermission(int uid, int deptid) {
		return this.userDeptRepo.getAllDeptpermission(uid, deptid);
	}

}