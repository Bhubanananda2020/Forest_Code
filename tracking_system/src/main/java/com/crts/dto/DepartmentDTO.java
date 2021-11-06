package com.crts.dto;

import java.util.List;

import com.crts.entity.DeptEntity;

public class DepartmentDTO {
	
	
	private DeptEntity deptEntity;
	private List<DeptEntity> lstdeptEntity;
	private String role;
	private String message;
	public DeptEntity getDeptEntity() {
		return deptEntity;
	}
	public String getRole() {
		return role;
	}
	public String getMessage() {
		return message;
	}
	public void setDeptEntity(DeptEntity deptEntity) {
		this.deptEntity = deptEntity;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<DeptEntity> getLstdeptEntity() {
		return lstdeptEntity;
	}
	public void setLstdeptEntity(List<DeptEntity> lstdeptEntity) {
		this.lstdeptEntity = lstdeptEntity;
	}
	
	
	
	

}
