package com.crts.serviceimpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;
import com.crts.entity.StatusEntityview;
import com.crts.repo.StatusRepo;
import com.crts.service.StatusService;

@Service
public class StatusServiceImpl implements StatusService {

	@Autowired
	private StatusRepo statusRepo;

	/* ==== Save Status ==== */
	public StatusEntity saveStatus(StatusEntity se) {
		return this.statusRepo.save(se);
	}

	/* ==== Get Status ==== */
	public List<StatusEntity> getAllStatus() {
		return this.statusRepo.findAll();
	}

	/* ==== Get Latest Status By requestNumber ==== */
	public StatusEntity getStatusByRequestNumber(int reqnum) {
		return this.statusRepo.getStatusByRequestNumber(reqnum);
	}

	@Override
	public List<StatusEntity> getAllCreateBy(int createby) {
		return this.statusRepo.getStatusByReqcreateby(createby);
	}

	/*
	 * ==================================== User request work
	 * ============================================
	 */

	/* ======== Get ALL ARRISED LATEST Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllArrisedLastUpdateRequest(int uid) {
		List<Object[]> allstatus1 = this.statusRepo.getAllArrisedLastUpdateRequest(uid);
		List<StatusEntityview> allarrisestatus = new ArrayList<StatusEntityview>();
		for (Object[] obj1 : allstatus1) {
			String reqcode = (String) obj1[0];
			String reqtitle = ((String) obj1[1]);
			String status_desc = ((String) obj1[2]);
			int reqassignto = (int) (obj1[3]);
			Date date = (Date) obj1[4];
			int severity1 = (int) (obj1[5]);
			int piority1 = (int) (obj1[6]);
			String username1 = (String) (obj1[9]);
			int age;
			if ((BigInteger) obj1[7] != null) {
				BigInteger age21 = (BigInteger) obj1[7];
				age = age21.intValue();
			} else {
				age = 0;
			}
			StatusEntityview ue1 = new StatusEntityview(reqcode, reqtitle, status_desc, reqassignto, date, severity1,
					piority1, age, username1);
			allarrisestatus.add(ue1);
		}
		return allarrisestatus;
	}

	/* ======== Get ALL ASSIGN LATEST Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequest(int uid) {
		List<Object[]> allstatus2 = this.statusRepo.getAllAssignLastUpdateRequest(uid);
		List<StatusEntityview> allassignstatus = new ArrayList<StatusEntityview>();
		for (Object[] obj2 : allstatus2) {
			String reqcode = (String) obj2[0];
			String reqtitle = ((String) obj2[1]);
			String status_desc = ((String) obj2[2]);
			int reqassignto = (int) (obj2[3]);
			Date date = (Date) obj2[4];
			int severity2 = (int) (obj2[5]);
			int piority2 = (int) (obj2[6]);
			String username2 = (String) (obj2[9]);
			int age;
			if ((BigInteger) obj2[7] != null) {
				BigInteger age22 = (BigInteger) obj2[7];
				age = age22.intValue();
			} else {
				age = 0;
			}
			StatusEntityview ue2 = new StatusEntityview(reqcode, reqtitle, status_desc, reqassignto, date, severity2,
					piority2, age, username2);
			allassignstatus.add(ue2);
		}
		return allassignstatus;
	}

	/* ======== Get ALL RISED CLOSED Request By LOGIN USER ID ======== */
	public List<StatusEntityview> getAllArrisedClosedRequest(int uid) {
		List<Object[]> allstatus3 = this.statusRepo.getAllArrisedClosedRequest(uid);
		List<StatusEntityview> allarriseclosedstatus = new ArrayList<StatusEntityview>();
		System.out.println(allstatus3);
		for (Object[] obj3 : allstatus3) {
			String reqcode = (String) obj3[0];
			String reqtitle = ((String) obj3[1]);
			String status_desc = ((String) obj3[2]);
			int reqassignto = (int) (obj3[3]);
			Date date = (Date) obj3[4];
			int severity3 = (int) (obj3[5]);
			int piority3 = (int) (obj3[6]);
			String username3 = (String) (obj3[9]);
			int age;
			if ((BigInteger) obj3[7] != null) {
				BigInteger age23 = (BigInteger) obj3[7];
				age = age23.intValue();
			} else {
				age = 0;
			}
			StatusEntityview ue3 = new StatusEntityview(reqcode, reqtitle, status_desc, reqassignto, date, severity3,
					piority3, age, username3);
			allarriseclosedstatus.add(ue3);
		}
		return allarriseclosedstatus;
	}

	/*
	 * ==================================== Admin request view work
	 * ============================================
	 */

	/* ======== Get ALL ARRISED LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedLastUpdateRequestforadmin(int uid) {
		List<Object[]> allstatusadmin1 = this.statusRepo.getAllRaisedLastUpdateRequestforadmin(uid);
		List<StatusEntityview> allarrisestatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj1 : allstatusadmin1) {
			int createdBy = (int) obj1[0];
			String reqcode = ((String) obj1[1]);
			String reqtitle = ((String) obj1[2]);
			String status_desc = ((String) obj1[3]);
			String deptname = ((String) obj1[4]);
			String firstname = ((String) obj1[5]);
			int reqassignto = (int) (obj1[6]);
			Date date = (Date) obj1[7];
			int severity1 = (int) (obj1[8]);
			int piority1 = (int) (obj1[9]);
			int age;
			if ((BigInteger) obj1[10] != null) {
				BigInteger age21 = (BigInteger) obj1[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue1 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allarrisestatusadmin.add(ue1);
		}
		return allarrisestatusadmin;
	}

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequestforadmin(int uid) {
		List<Object[]> allstatusadmin2 = this.statusRepo.getAllRaisedLastUpdateRequestforadmin(uid);
		List<StatusEntityview> allassignstatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj2 : allstatusadmin2) {
			int createdBy = (int) obj2[0];
			String reqcode = ((String) obj2[1]);
			String reqtitle = ((String) obj2[2]);
			String status_desc = ((String) obj2[3]);
			String deptname = ((String) obj2[4]);
			String firstname = ((String) obj2[5]);
			int reqassignto = (int) (obj2[6]);
			Date date = (Date) obj2[7];
			int severity1 = (int) (obj2[8]);
			int piority1 = (int) (obj2[9]);
			int age;
			if ((BigInteger) obj2[10] != null) {
				BigInteger age21 = (BigInteger) obj2[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue2 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allassignstatusadmin.add(ue2);
		}
		return allassignstatusadmin;
	}

	/* ======== Get ALL ARRISED CLOSED Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedClosedRequestforadmin(int uid) {
		List<Object[]> allstatusadmin3 = this.statusRepo.getAllRaisedClosedRequestforadmin(uid);
		List<StatusEntityview> allassignstatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj3 : allstatusadmin3) {
			int createdBy = (int) obj3[0];
			String reqcode = ((String) obj3[1]);
			String reqtitle = ((String) obj3[2]);
			String status_desc = ((String) obj3[3]);
			String deptname = ((String) obj3[4]);
			String firstname = ((String) obj3[5]);
			int reqassignto = (int) (obj3[6]);
			Date date = (Date) obj3[7];
			int severity1 = (int) (obj3[8]);
			int piority1 = (int) (obj3[9]);
			int age;
			if ((BigInteger) obj3[10] != null) {
				BigInteger age21 = (BigInteger) obj3[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue3 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allassignstatusadmin.add(ue3);
		}
		return allassignstatusadmin;
	}

	

	/* ==================================== Super Admin request view work ============================================ */
	
	/* ======== Get ALL ARRISED LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedLastUpdateRequestforsuperadmin() {
		List<Object[]> allstatusadmin1 = this.statusRepo.getAllRaisedLastUpdateRequestforsuperadmin();
		List<StatusEntityview> allarrisestatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj1 : allstatusadmin1) {
			int createdBy = (int) obj1[0];
			String reqcode = ((String) obj1[1]);
			String reqtitle = ((String) obj1[2]);
			String status_desc = ((String) obj1[3]);
			String deptname = ((String) obj1[4]);
			String firstname = ((String) obj1[5]);
			int reqassignto = (int) (obj1[6]);
			Date date = (Date) obj1[7];
			int severity1 = (int) (obj1[8]);
			int piority1 = (int) (obj1[9]);
			int age;
			if ((BigInteger) obj1[10] != null) {
				BigInteger age21 = (BigInteger) obj1[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue1 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allarrisestatusadmin.add(ue1);
		}
		return allarrisestatusadmin;
	}

	
	/* ======== Get ALL ASSIGN LATEST Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllAssignLastUpdateRequestforsuperadmin() {
		List<Object[]> allstatusadmin2 = this.statusRepo.getAllAssignLastUpdateRequestforsuperadmin();
		List<StatusEntityview> allassignstatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj2 : allstatusadmin2) {
			int createdBy = (int) obj2[0];
			String reqcode = ((String) obj2[1]);
			String reqtitle = ((String) obj2[2]);
			String status_desc = ((String) obj2[3]);
			String deptname = ((String) obj2[4]);
			String firstname = ((String) obj2[5]);
			int reqassignto = (int) (obj2[6]);
			Date date = (Date) obj2[7];
			int severity1 = (int) (obj2[8]);
			int piority1 = (int) (obj2[9]);
			int age;
			if ((BigInteger) obj2[10] != null) {
				BigInteger age21 = (BigInteger) obj2[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue2 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allassignstatusadmin.add(ue2);
		}
		return allassignstatusadmin;
	}

	/* ======== Get ALL ARRISED CLOSED Request By LOGIN Admin ID ======== */
	public List<StatusEntityview> getAllRaisedClosedRequestforsuperadmin() {
		List<Object[]> allstatusadmin3 = this.statusRepo.getAllRaisedClosedRequestforsuperadmin();
		List<StatusEntityview> allassignstatusadmin = new ArrayList<StatusEntityview>();
		for (Object[] obj3 : allstatusadmin3) {
			int createdBy = (int) obj3[0];
			String reqcode = ((String) obj3[1]);
			String reqtitle = ((String) obj3[2]);
			String status_desc = ((String) obj3[3]);
			String deptname = ((String) obj3[4]);
			String firstname = ((String) obj3[5]);
			int reqassignto = (int) (obj3[6]);
			Date date = (Date) obj3[7];
			int severity1 = (int) (obj3[8]);
			int piority1 = (int) (obj3[9]);
			int age;
			if ((BigInteger) obj3[10] != null) {
				BigInteger age21 = (BigInteger) obj3[10];
				age = age21.intValue();
			} else {
				age = 0;
			}			
			StatusEntityview ue3 = new StatusEntityview(createdBy, reqcode, reqtitle, status_desc, deptname, firstname, reqassignto, date, severity1, piority1, age);
			allassignstatusadmin.add(ue3);
		}
		return allassignstatusadmin;
	}


}
