package com.crts.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "user_dept")
public class UserDeptEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_dept_id")
	private int userdeptid;


	@Column(name = "user_id")
	private int userid;
	
	
	  @Column(name = "Dept_id") private int deptid;
	 
	
	@Column(name = "created_date")
	private Date createddate;

	@Column(name = "created_By")
	private int decreatedby;

	@Column(name = "user_role")
	private String role;

	
	
	
	
	/*
	 * @ManyToOne(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "Dept_id")
	 * 
	 * @JsonBackReference private DeptEntity deptEntity;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public DeptEntity getDeptEntity() { return deptEntity; }
	 * 
	 * public void setDeptEntity(DeptEntity deptEntity) { this.deptEntity =
	 * deptEntity; }
	 * 
	 * 
	 */
	public int getUserdeptid() {
		return userdeptid;
	}

	public void setUserdeptid(int userdeptid) {
		this.userdeptid = userdeptid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	
	  public int getDeptid() { return deptid; }
	  
	  public void setDeptid(int deptid) { this.deptid = deptid; }
	 

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public int getDecreatedby() {
		return decreatedby;
	}

	public void setDecreatedby(int decreatedby) {
		this.decreatedby = decreatedby;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDeptEntity [userdeptid=" + userdeptid + ", userid=" + userid + ", deptid=" + deptid
				+ ", createddate=" + createddate + ", decreatedby=" + decreatedby + ", role=" + role + "]";
	}


	
	
	
	
	
	
	
	
	
	
	
}
