package com.crts.service;

import java.util.List;

import com.crts.entity.DeptEntity;



public interface DeptService {


	/* ============ SAVE DEPARTMENT ============ */
	public boolean saveDepartment(String dcode,String dname,String dpcode,int createby, String diact);
	
	/* ============ GET ALL PARENT DEPARTMENT CODE ============ */
	public List<String> getAllParentDeptCodeAndUid(int uId);

	/* ============ GET ALL PARENT DEPARTMENT CODE ============ */
	public List<String> getAllParentDeptCode();	
	
	
	/* ============ GET ALL DEPARTMENT CODE ============ */
	public List<String> getAllDeptCode();
	
	/* ============ GET ALL DEPARTMENT FOR SUPER ADMIN ============ */
	public List<DeptEntity> getAllDept();

	/* ============ GET ALL DEPARTMENT FOR ADMIN============ */	
	public List<DeptEntity> getAllDeptAdmin(int uId);
	
	
	/* ============ GET DEPARTMENT CODE By Code ============ */
	public List<DeptEntity> getAllDeptEntityByCode(String deptcode);

	
		
	/* ============ GET ALL CREATE USER BY DEPTCODE ============ */
	public List<DeptEntity> getDeptUserByDeptcode(String deptcode);

	
	
	/* ============ GET DEPARTMENT ID By DEPT CODE ============ */
	public int getDeptIDByDeptCode(String deptcode);
	
	
	/* ============ GET DEPARTMENT BY DEPARTMENT ID ============ */	
	public DeptEntity getBydeid(int deid);
	
	
	
}
