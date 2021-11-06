package com.crts.entity;

public class ResponseDept {
	
	private String deptCode;
	private String deptName;
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@Override
	public String toString() {
		return "ResponseDept [deptCode=" + deptCode + ", deptName=" + deptName + "]";
	}
	public ResponseDept(String deptCode, String deptName) {
		super();
		this.deptCode = deptCode;
		this.deptName = deptName;
	}
	public ResponseDept() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
