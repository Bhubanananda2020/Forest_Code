package com.crts.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.crts.entity.StatusEntity;
import com.crts.entity.StatusEntityview;

@Service
public interface StatusService {

	/* ==== Save Status ==== */
	public StatusEntity saveStatus(StatusEntity se);	
	
	/* ==== Get Status ==== */
	public List<StatusEntity> getAllStatus();	
	
	/* ==== Get Latest Status By requestNumber ==== */ 
	public StatusEntity getStatusByRequestNumber(int reqnum);	
	
	/* ==== Get Status BY CREATEbY  ==== */
	public List<StatusEntity> getAllCreateBy(int createby);	
	
	
	/* ==================================== User request work ============================================ */
	
	/* ======== Get ALL RAISED LATEST Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllArrisedLastUpdateRequest(int uid);

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequest(int uid);

	/* ======== Get ALL RAISED CLOSED Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllArrisedClosedRequest(int uid);

	
	/* ==================================== Admin request view work ============================================ */
	
	/* ======== Get ALL RAISED LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedLastUpdateRequestforadmin(int uid);

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequestforadmin(int uid);

	/* ======== Get ALL RAISED CLOSED Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedClosedRequestforadmin(int uid);

	

	/* ==================================== Super Admin request view work ============================================ */
	
	/* ======== Get ALL RAISED LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedLastUpdateRequestforsuperadmin();

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequestforsuperadmin();

	/* ======== Get ALL RAISED CLOSED Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedClosedRequestforsuperadmin();

	
	
	
}
