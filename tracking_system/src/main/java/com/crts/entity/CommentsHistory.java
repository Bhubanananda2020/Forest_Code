package com.crts.entity;

import java.util.Date;

public class CommentsHistory {

	

	private String requesttitle;
	private String userfirstname;
	private Date createddate;
	private String commentsdesc;
	
	
	
	
	
	
	
	public String getRequesttitle() {
		return requesttitle;
	}
	public void setRequesttitle(String requesttitle) {
		this.requesttitle = requesttitle;
	}
	public String getUserfirstname() {
		return userfirstname;
	}
	public void setUserfirstname(String userfirstname) {
		this.userfirstname = userfirstname;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public String getCommentsdesc() {
		return commentsdesc;
	}
	public void setCommentsdesc(String commentsdesc) {
		this.commentsdesc = commentsdesc;
	}
	@Override
	public String toString() {
		return "CommentsHistory [requesttitle=" + requesttitle + ", userfirstname=" + userfirstname + ", createddate="
				+ createddate + ", commentsdesc=" + commentsdesc + "]";
	}
	public CommentsHistory(String requesttitle, String userfirstname, Date createddate, String commentsdesc) {
		super();
		this.requesttitle = requesttitle;
		this.userfirstname = userfirstname;
		this.createddate = createddate;
		this.commentsdesc = commentsdesc;
	}
	public CommentsHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	
	
	
	
	
	
	
}
