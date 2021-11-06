package com.crts.service;


import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.crts.entity.CommentsHistory;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;

@Service
public interface RequestService {

	/* ======== Generate Request Code ======== */
	public String getLastRequestNumberByDeptId(String deptcode);
	
	
	/* ======== Save Request ======== */
	public RequestEntity saveRequest(RequestEntity requestEntity);

	
	/* ======== Get Request By Request Code ======== */
	public RequestEntity getRequestByReqcode(String rcode);

	/* ======== Update Request ======== */
	public RequestEntity updateRequest(RequestEntity re, StatusEntity se, int userid);

	/* ===== Get All comments By Request Id and user id ===== */
	public List<CommentsHistory> getAllCommentByReqId(String reqnum);

	/* ========= No of raised Request for Department=============== */
	public int noOfRaisedRequestInDepartment(int uid);
	
	/* ========= No of Complete Request for Department=============== */
	public int noOfAssignedRequestInDepartment(int uid);
	
	
	
}