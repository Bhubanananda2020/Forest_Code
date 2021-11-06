package com.forest_code.dto;

public class EmployeeDTO {

	private String ename;
	private String eemail;
	private String eposition;
	private Long esalary;
	private String eusername;
	private String epassword;
	private String eroll;
	private boolean isEnabled;
	@Override
	public String toString() {
		return "EmployeeDTO [ename=" + ename + ", eemail=" + eemail + ", eposition=" + eposition + ", esalary="
				+ esalary + ", eusername=" + eusername + ", epassword=" + epassword + ", eroll=" + eroll
				+ ", isEnabled=" + isEnabled + "]";
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
