package com.crts.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class StatusEntityview {

	private int createdBy;
	private String reqcode;
	private String reqtitle;
	private String status_desc;
	private String deptname;
	private String firstname;
	private int reqassignto;
	private Date date;
	private int severity1;
	private int piority1;
	private int age;

	
	
	
	
	
		
	
	public int getCreatedBy() {
		return createdBy;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getReqcode() {
		return reqcode;
	}
	public String getReqtitle() {
		return reqtitle;
	}
	public String getStatus_desc() {
		return status_desc;
	}
	public int getReqassignto() {
		return reqassignto;
	}
	public Date getDate() {
		return date;
	}
	public int getSeverity1() {
		return severity1;
	}
	public int getPiority1() {
		return piority1;
	}
	public int getAge() {
		return age;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setReqcode(String reqcode) {
		this.reqcode = reqcode;
	}
	public void setReqtitle(String reqtitle) {
		this.reqtitle = reqtitle;
	}
	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}
	public void setReqassignto(int reqassignto) {
		this.reqassignto = reqassignto;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setSeverity1(int severity1) {
		this.severity1 = severity1;
	}
	public void setPiority1(int piority1) {
		this.piority1 = piority1;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}





	public StatusEntityview(String reqcode, String reqtitle, String status_desc, int reqassignto, Date date,
			int severity1, int piority1, int age, String firstname) {
		super();
		this.reqcode = reqcode;
		this.reqtitle = reqtitle;
		this.status_desc = status_desc;
		this.reqassignto = reqassignto;
		this.date = date;
		this.severity1 = severity1;
		this.piority1 = piority1;
		this.age = age;
		this.firstname = firstname;
	}

	public StatusEntityview(int createdBy, String reqcode, String reqtitle, String status_desc, String deptname,
			String firstname, int reqassignto, Date date, int severity1, int piority1, int age) {
		super();
		this.createdBy = createdBy;
		this.reqcode = reqcode;
		this.reqtitle = reqtitle;
		this.status_desc = status_desc;
		this.deptname = deptname;
		this.firstname = firstname;
		this.reqassignto = reqassignto;
		this.date = date;
		this.severity1 = severity1;
		this.piority1 = piority1;
		this.age = age;
	}
	@Override
	public String toString() {
		return "StatusEntityview [createdBy=" + createdBy + ", reqcode=" + reqcode + ", reqtitle=" + reqtitle
				+ ", status_desc=" + status_desc + ", deptname=" + deptname + ", firstname=" + firstname
				+ ", reqassignto=" + reqassignto + ", date=" + date + ", severity1=" + severity1 + ", piority1="
				+ piority1 + ", age=" + age + "]";
	}

	
	
	
	

	
	
	
	
}
