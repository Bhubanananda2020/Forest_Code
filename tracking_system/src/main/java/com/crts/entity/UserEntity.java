package com.crts.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_entity")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "User_id")
	private int uId;

	@NotBlank(message = "Please enter username !! ")
	@Size(min = 2, max = 25, message = "Minimun 2 and maximun 25 charactes are Required !! ")
	@Column(length = 25, name = "Username")
	private String uName;

	@Column(length = 25, name = "User_First_Name")
	private String uFName;

	@Column(length = 25, name = "User_Last_Name")
	private String uLName;

	@Column(length = 25, name = "User_email")
	private String uEmail;

	@Column(length = 250, name = "User_Password")
	@NotBlank(message = "Please enter Password !! ")
	@Size(min = 8, max = 250, message = "Minimun 8 and maximun 250 charactes are allowed !! ")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!=])(?=\\S+$).{8,}$", message = "Password must contain all character")
	private String uPassword;

	@Column(name = "Created_Date")
	private Date uCDate;

	@Column(length = 25, name = "Created_By")
	private String uCBy;

	@Column(name = "Is_User_Active")
	private boolean uIsActive;

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuFName() {
		return uFName;
	}

	public void setuFName(String uFName) {
		this.uFName = uFName;
	}

	public String getuLName() {
		return uLName;
	}

	public void setuLName(String uLName) {
		this.uLName = uLName;
	}

	public String getuEmail() {
		return uEmail;
	}

	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public Date getuCDate() {
		return uCDate;
	}

	public void setuCDate(Date uCDate) {
		this.uCDate = uCDate;
	}

	public String getuCBy() {
		return uCBy;
	}

	public void setuCBy(String uCBy) {
		this.uCBy = uCBy;
	}

	public boolean isuIsActive() {
		return uIsActive;
	}

	public void setuIsActive(boolean uIsActive) {
		this.uIsActive = uIsActive;
	}

	@Override
	public String toString() {
		return "UserEntity [uId=" + uId + ", uName=" + uName + ", uFName=" + uFName + ", uLName=" + uLName + ", uEmail="
				+ uEmail + ", uPassword=" + uPassword + ", uCDate=" + uCDate + ", uCBy=" + uCBy + ", uIsActive="
				+ uIsActive + "]";
	}

	public UserEntity() {
		super();
	}

	public UserEntity(int uId, String uFName) {
		super();
		this.uId = uId;
		this.uFName = uFName;
	}


	
	
	

	
	
	
	
	
	
	
}
