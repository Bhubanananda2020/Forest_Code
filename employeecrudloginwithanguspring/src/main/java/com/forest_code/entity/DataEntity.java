package com.forest_code.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String userName;
    private String uDob;
    private String uGender;
    private String uDesignation;
    private String uCompanyName;
    private String uEmail;
    private String uNumber;
    private String uAddress;
    
    
    
    
	public DataEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DataEntity(int id, String userName, String uDob, String uGender, String uDesignation, String uCompanyName,
			String uEmail, String uNumber, String uAddress) {
		super();
		this.id = id;
		this.userName = userName;
		this.uDob = uDob;
		this.uGender = uGender;
		this.uDesignation = uDesignation;
		this.uCompanyName = uCompanyName;
		this.uEmail = uEmail;
		this.uNumber = uNumber;
		this.uAddress = uAddress;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getuDob() {
		return uDob;
	}
	public void setuDob(String uDob) {
		this.uDob = uDob;
	}
	public String getuGender() {
		return uGender;
	}
	public void setuGender(String uGender) {
		this.uGender = uGender;
	}
	public String getuDesignation() {
		return uDesignation;
	}
	public void setuDesignation(String uDesignation) {
		this.uDesignation = uDesignation;
	}
	public String getuCompanyName() {
		return uCompanyName;
	}
	public void setuCompanyName(String uCompanyName) {
		this.uCompanyName = uCompanyName;
	}
	public String getuEmail() {
		return uEmail;
	}
	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}
	public String getuNumber() {
		return uNumber;
	}
	public void setuNumber(String uNumber) {
		this.uNumber = uNumber;
	}
	public String getuAddress() {
		return uAddress;
	}
	public void setuAddress(String uAddress) {
		this.uAddress = uAddress;
	}
	@Override
	public String toString() {
		return "DataEntity [id=" + id + ", userName=" + userName + ", uDob=" + uDob + ", uGender=" + uGender
				+ ", uDesignation=" + uDesignation + ", uCompanyName=" + uCompanyName + ", uEmail=" + uEmail
				+ ", uNumber=" + uNumber + ", uAddress=" + uAddress + "]";
	}
    


	
    
    
    
}
