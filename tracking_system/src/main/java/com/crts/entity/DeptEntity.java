package com.crts.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "dept_entity")
public class DeptEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Dept_id")
	private int deid;

	@NotEmpty(message = "Department Code Required !!")
	@Column(length = 25, name = "dept_code")
	private String decode;

	@NotEmpty(message = "Department Name Required !!")
	@Size(min = 4, max = 25, message = "Minimun Length 4 and maximun 25 charactes are Required !!")
	@Column(length = 25, name = "department_name")
	private String dename;

	@NotEmpty(message = "Parent Dept Code Required !!")
	@Column(length = 25, name = "parent_department_code")
	private String depcode;

	@Column(name = "created_date")
	private Date decdate;

	@Column(name = "created_By")
	private int decreatedby;

	@Column(name = "is_dept_active")
	private boolean deisactive;

	
	/*
	 * @OneToMany(mappedBy = "deptEntity", cascade = CascadeType.ALL)
	 * 
	 * @JsonManagedReference private List<UserDeptEntity> userDeptEntity;
	 */
	
	
	
	
	
	/*
	 * public List<UserDeptEntity> getUserDeptEntity() { return userDeptEntity; }
	 * 
	 * public void setUserDeptEntity(List<UserDeptEntity> userDeptEntity) {
	 * this.userDeptEntity = userDeptEntity; }
	 */
	public int getDeid() {
		return deid;
	}

	public void setDeid(int deid) {
		this.deid = deid;
	}

	public String getDecode() {
		return decode;
	}

	public void setDecode(String decode) {
		this.decode = decode;
	}

	public String getDename() {
		return dename;
	}

	public void setDename(String dename) {
		this.dename = dename;
	}

	public String getDepcode() {
		return depcode;
	}

	public void setDepcode(String depcode) {
		this.depcode = depcode;
	}

	public Date getDecdate() {
		return decdate;
	}

	public void setDecdate(Date decdate) {
		this.decdate = decdate;
	}

	public int getDecreatedby() {
		return decreatedby;
	}

	public void setDecreatedby(int decreatedby) {
		this.decreatedby = decreatedby;
	}

	public boolean isDeisactive() {
		return deisactive;
	}

	public void setDeisactive(boolean deisactive) {
		this.deisactive = deisactive;
	}

	@Override
	public String toString() {
		return "DeptEntity [deid=" + deid + ", decode=" + decode + ", dename=" + dename + ", depcode=" + depcode
				+ ", decdate=" + decdate + ", decreatedby=" + decreatedby + ", deisactive=" + deisactive + "]";
	}


}