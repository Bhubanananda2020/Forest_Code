package com.crts.entity;

import java.util.Date;
import java.util.List;

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

import javax.persistence.CascadeType;

@Entity
@Table(name = "requests_entity")
public class RequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int reqid;

	@NotEmpty(message = "Department Code Required !!")
	@Size(min = 4, max = 6, message = "Department Most Have Minimun Length 4 and maximun 6 charactes are Required !!")
	@Column(length = 25, name = "request_dept")
	private String reqdeptcode;

	@Column(length = 25, name = "request_number")
	private String reqcode;

	@NotEmpty(message = "Request title can't empty!!")
	@Size(min = 4, max = 20, message = "Minimun Length 4 and maximun 20 charactes are Required !!")
	@Column(length = 25, name = "request_title")
	private String reqtitle;

	@NotEmpty(message = "Request Description Can't Empty!!")
	@Size(min = 10, max = 180, message = "Minimun Length 10 and maximun 180 charactes are Required !!")
	@Column(length = 200, name = "request_description")
	private String reqdesc;

	@Column(name = "assigned_to")
	private int reqassignto;

	@Column(name = "assigned_date")
	private Date reqassigndate;

	@Column(length = 255, name = "initial_comments")
	private String reqinicomment;

	@Column(name = "severity")
	private int severity;
	
	@Column(name = "piority")
	private int piority;
	
	@Column(name = "created_By")
	private int recreatedby;
	
	
	
	

	@OneToMany(mappedBy = "requestEntity", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<StatusEntity> statusEntity;

	@OneToMany(mappedBy = "requestEntity", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<CommentsEntity> cCommentsEntity;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getPiority() {
		return piority;
	}

	public void setPiority(int piority) {
		this.piority = piority;
	}



	public int getReqid() {
		return reqid;
	}

	public void setReqid(int reqid) {
		this.reqid = reqid;
	}

	public String getReqdeptcode() {
		return reqdeptcode;
	}

	public void setReqdeptcode(String reqdeptcode) {
		this.reqdeptcode = reqdeptcode;
	}

	public String getReqcode() {
		return reqcode;
	}

	public void setReqcode(String reqcode) {
		this.reqcode = reqcode;
	}

	public String getReqtitle() {
		return reqtitle;
	}

	public void setReqtitle(String reqtitle) {
		this.reqtitle = reqtitle;
	}

	public String getReqdesc() {
		return reqdesc;
	}

	public void setReqdesc(String reqdesc) {
		this.reqdesc = reqdesc;
	}

	public int getReqassignto() {
		return reqassignto;
	}

	public void setReqassignto(int reqassignto) {
		this.reqassignto = reqassignto;
	}

	public Date getReqassigndate() {
		return reqassigndate;
	}

	public void setReqassigndate(Date reqassigndate) {
		this.reqassigndate = reqassigndate;
	}

	public String getReqinicomment() {
		return reqinicomment;
	}

	public void setReqinicomment(String reqinicomment) {
		this.reqinicomment = reqinicomment;
	}

	public int getRecreatedby() {
		return recreatedby;
	}

	public void setRecreatedby(int recreatedby) {
		this.recreatedby = recreatedby;
	}

	public List<StatusEntity> getStatusEntity() {
		return statusEntity;
	}

	public void setStatusEntity(List<StatusEntity> statusEntity) {
		this.statusEntity = statusEntity;
	}

	public List<CommentsEntity> getcCommentsEntity() {
		return cCommentsEntity;
	}

	public void setcCommentsEntity(List<CommentsEntity> cCommentsEntity) {
		this.cCommentsEntity = cCommentsEntity;
	}

	@Override
	public String toString() {
		return "RequestEntity [reqid=" + reqid + ", reqdeptcode=" + reqdeptcode + ", reqcode=" + reqcode + ", reqtitle="
				+ reqtitle + ", reqdesc=" + reqdesc + ", reqassignto=" + reqassignto + ", reqassigndate="
				+ reqassigndate + ", reqinicomment=" + reqinicomment + ", recreatedby=" + recreatedby
				+ ", statusEntity=" + statusEntity + ", cCommentsEntity=" + cCommentsEntity + "]";
	}

}