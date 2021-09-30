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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "status_entity")
public class StatusEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id")
	private int seid;

	@Column(name = "status_code")
	private char sescode;

	@Column(length = 25, name = "status_desc")
	private String sestdesc;

	@Column(name = "created_date")
	private Date reqdate;

	@Column(name = "created_by")
	private int reqcreateby;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "request_number")
	@JsonBackReference
	private RequestEntity requestEntity;
	
	/*
	 * @OneToOne(mappedBy = "stausEntity",cascade = CascadeType.ALL) private
	 * CommentsEntity commentsEntity;
	 */
	
	
	
	
	public int getSeid() {
		return seid;
	}

	public void setSeid(int seid) {
		this.seid = seid;
	}

	public char getSescode() {
		return sescode;
	}

	public void setSescode(char sescode) {
		this.sescode = sescode;
	}

	public String getSestdesc() {
		return sestdesc;
	}

	public void setSestdesc(String sestdesc) {
		this.sestdesc = sestdesc;
	}

	public Date getReqdate() {
		return reqdate;
	}

	public void setReqdate(Date reqdate) {
		this.reqdate = reqdate;
	}

	public int getReqcreateby() {
		return reqcreateby;
	}

	public void setReqcreateby(int reqcreateby) {
		this.reqcreateby = reqcreateby;
	}

	public RequestEntity getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(RequestEntity requestEntity) {
		this.requestEntity = requestEntity;
	}

	/*
	 * public CommentsEntity getCommentsEntity() { return commentsEntity; }
	 * 
	 * public void setCommentsEntity(CommentsEntity commentsEntity) {
	 * this.commentsEntity = commentsEntity; }
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public StatusEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatusEntity(String sestdesc, Date reqdate, RequestEntity requestEntity) {
		super();
		this.sestdesc = sestdesc;
		this.reqdate = reqdate;
		this.requestEntity = requestEntity;
	}

	@Override
	public String toString() {
		return "StatusEntity [seid=" + seid + ", sescode=" + sescode + ", sestdesc=" + sestdesc + ", reqdate=" + reqdate
				+ ", reqcreateby=" + reqcreateby + "]";
	}



	/*
	 * public StatusEntity(int seid, char sescode, String sestdesc, Date reqdate,
	 * int reqcreateby, RequestEntity requestEntity, CommentsEntity commentsEntity)
	 * { super(); this.seid = seid; this.sescode = sescode; this.sestdesc =
	 * sestdesc; this.reqdate = reqdate; this.reqcreateby = reqcreateby;
	 * this.requestEntity = requestEntity; this.commentsEntity = commentsEntity; }
	 * 
	 * @Override public String toString() { return "StatusEntity [seid=" + seid +
	 * ", sescode=" + sescode + ", sestdesc=" + sestdesc + ", reqdate=" + reqdate +
	 * ", reqcreateby=" + reqcreateby + ", requestEntity=" + requestEntity +
	 * ", commentsEntity=" + commentsEntity + "]"; }
	 */
		
	
	
	
	


	
	
	
	
	
}
