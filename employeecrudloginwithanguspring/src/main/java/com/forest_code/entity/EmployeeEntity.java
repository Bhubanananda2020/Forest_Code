package com.forest_code.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmployeeEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eid;
	private String ename;
	private String eemail;
	private String eposition;
	private Long esalary;
	private String eusername;
	private String epassword;
	private String eroll;
	private boolean isEnabled;
	
	
	public EmployeeEntity(int eid, String ename, String eemail, String eposition, Long esalary, String eusername,
			String epassword, String eroll, boolean isEnabled) {
		super();
		this.eid = eid;
		this.ename = ename;
		this.eemail = eemail;
		this.eposition = eposition;
		this.esalary = esalary;
		this.eusername = eusername;
		this.epassword = epassword;
		this.eroll = eroll;
		this.isEnabled = isEnabled;
	}

	
	public EmployeeEntity() {
		super();
	}

	@Override
	public String toString() {
		return "EmployeeEntity [eid=" + eid + ", ename=" + ename + ", eemail=" + eemail + ", eposition=" + eposition
				+ ", esalary=" + esalary + ", eusername=" + eusername + ", epassword=" + epassword + ", eroll=" + eroll
				+ ", isEnabled=" + isEnabled + "]";
	}
	
	
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEemail() {
		return eemail;
	}
	public void setEemail(String eemail) {
		this.eemail = eemail;
	}
	public String getEposition() {
		return eposition;
	}
	public void setEposition(String eposition) {
		this.eposition = eposition;
	}
	public Long getEsalary() {
		return esalary;
	}
	public void setEsalary(Long esalary) {
		this.esalary = esalary;
	}
	public String getEusername() {
		return eusername;
	}
	public void setEusername(String eusername) {
		this.eusername = eusername;
	}
	public String getEpassword() {
		return epassword;
	}
	public void setEpassword(String epassword) {
		this.epassword = epassword;
	}
	public String getEroll() {
		return eroll;
	}
	public void setEroll(String eroll) {
		this.eroll = eroll;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}