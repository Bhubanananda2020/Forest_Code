package com.crts.entity;

import java.util.Date;

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
@Table(name = "comments_entity")
public class CommentsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comments_id")
	private int cmid;
	/*
	 * @Column(name = "status_id") private int cmsid;
	 */

	@Column(length = 250, name = "comments_desc")
	private String cmdesc;

	@Column(name = "created_date")
	private Date cmreqdate;

	@Column(name = "created_By")
	private int cmreqcreateby;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "request_number")
	@JsonBackReference
	private RequestEntity requestEntity;

	
	/*
	 * @OneToOne(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "status_number") private StatusEntity stausEntity;
	 */

	public int getCmid() {
		return cmid;
	}


	public void setCmid(int cmid) {
		this.cmid = cmid;
	}

	/*
	 * public int getCmsid() { return cmsid; }
	 * 
	 * 
	 * public void setCmsid(int cmsid) { this.cmsid = cmsid; }
	 */

	public String getCmdesc() {
		return cmdesc;
	}


	public void setCmdesc(String cmdesc) {
		this.cmdesc = cmdesc;
	}


	public Date getCmreqdate() {
		return cmreqdate;
	}


	public void setCmreqdate(Date cmreqdate) {
		this.cmreqdate = cmreqdate;
	}


	public int getCmreqcreateby() {
		return cmreqcreateby;
	}


	public void setCmreqcreateby(int cmreqcreateby) {
		this.cmreqcreateby = cmreqcreateby;
	}


	public RequestEntity getRequestEntity() {
		return requestEntity;
	}


	public void setRequestEntity(RequestEntity requestEntity) {
		this.requestEntity = requestEntity;
	}

	/*
	 * public StatusEntity getStausEntity() { return stausEntity; }
	 * 
	 * 
	 * public void setStausEntity(StatusEntity stausEntity) { this.stausEntity =
	 * stausEntity; }
	 */

	

	public CommentsEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "CommentsEntity [cmid=" + cmid + ", cmdesc=" + cmdesc + ", cmreqdate=" + cmreqdate + ", cmreqcreateby="
				+ cmreqcreateby + "]";
	}





	/*
	 * public CommentsEntity(int cmid, int cmsid, String cmdesc, Date cmreqdate, int
	 * cmreqcreateby, RequestEntity requestEntity, StatusEntity stausEntity) {
	 * super(); this.cmid = cmid; this.cmsid = cmsid; this.cmdesc = cmdesc;
	 * this.cmreqdate = cmreqdate; this.cmreqcreateby = cmreqcreateby;
	 * this.requestEntity = requestEntity; this.stausEntity = stausEntity; }
	 * 
	 * 
	 * @Override public String toString() { return "CommentsEntity [cmid=" + cmid +
	 * ", cmsid=" + cmsid + ", cmdesc=" + cmdesc + ", cmreqdate=" + cmreqdate +
	 * ", cmreqcreateby=" + cmreqcreateby + ", requestEntity=" + requestEntity +
	 * ", stausEntity=" + stausEntity + "]"; }
	 */
	
	
	



	
	
	
	
	
}
