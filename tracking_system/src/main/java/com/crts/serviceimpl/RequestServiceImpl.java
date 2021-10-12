package com.crts.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crts.entity.CommentsHistory;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;
import com.crts.repo.RequestRepo;
import com.crts.service.RequestService;

@Service
public class RequestServiceImpl implements RequestService {

	@Autowired
	private RequestRepo requestRepo;

	/* ======== Generate Request Code ======== */
	public String getLastRequestNumberByDeptId(String deptcode) {
		String newrequnum = null;
		int lstnumber = 0;
		String LstReq = this.requestRepo.getLastRequestNumber(deptcode);
		if (LstReq != null) {
			lstnumber = Integer.parseInt(LstReq.substring(LstReq.length() - 5));
			if (lstnumber >= 0 && lstnumber < 9) {
				lstnumber = lstnumber + 1;
				newrequnum = "0000" + lstnumber;
			} else if (lstnumber >= 9 && lstnumber < 99) {
				lstnumber = lstnumber + 1;
				newrequnum = "000" + lstnumber;
			} else if (lstnumber >= 99 && lstnumber < 999) {
				lstnumber = lstnumber + 1;
				newrequnum = "00" + lstnumber;
			} else if (lstnumber >= 999 && lstnumber < 9999) {
				lstnumber = lstnumber + 1;
				newrequnum = "0" + lstnumber;
			} else if (lstnumber >= 9999 && lstnumber < 99999) {
				lstnumber = lstnumber + 1;
				newrequnum = Integer.toString(lstnumber);
			} else {
				System.out.println("invalid data");
			}
		} else {
			newrequnum = "00000";
		}
		return newrequnum;
	}

	/* ======== Save Request ======== */
	public RequestEntity saveRequest(String reqtitle, String reqdesc, String getNewRequestNum, String reqtodepart,
			int reqtoperson, String reqfstcomment,  int createby, int piority, int severity) {
		RequestEntity isSaveRequest = new RequestEntity();

		try {

			String reqstatus = "NEW REQUEST";
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);
			char stuscod = reqstatus.charAt(0);

			List<StatusEntity> selist = new ArrayList<StatusEntity>();
			StatusEntity se = new StatusEntity();
			se.setSescode(stuscod);
			se.setSestdesc(reqstatus);
			se.setReqdate(date);
			se.setReqcreateby(createby);
			selist.add(se);
			RequestEntity re = new RequestEntity();
			re.setReqdeptcode(reqtodepart);
			re.setReqcode(getNewRequestNum);
			re.setReqtitle(reqtitle);
			re.setReqdesc(reqdesc);
			re.setReqassignto(reqtoperson);
			re.setReqassigndate(date);
			re.setReqinicomment(reqfstcomment);
			re.setRecreatedby(createby);
			re.setStatusEntity(selist);
			re.setPiority(piority);
			re.setSeverity(severity);
			se.setRequestEntity(re);
			isSaveRequest = this.requestRepo.save(re);
			return isSaveRequest;
		} catch (Exception e) {
			return isSaveRequest = null;
		}
	}

	/* ======== Get Request By Request Code ======== */
	public RequestEntity getRequestByReqcode(String rcode) {
		return this.requestRepo.getByreqcode(rcode);
	}

	/* ======== Update Request ======== */
	@Transactional
	public RequestEntity updateRequest(RequestEntity re) {
		try {
			System.out.println("23");
			return this.requestRepo.saveAndFlush(re);
		} catch (Exception e) {
			System.out.println(e);
		}
		return re;
	}

	/* ===== Get All comments By Request Id and user id ===== */
	public List<CommentsHistory> getAllCommentByReqId(String reqnum) {

		List<Object[]> listaa = this.requestRepo.getAllCommentByReqId(reqnum);
		List<CommentsHistory> allCommentHistory = new ArrayList<CommentsHistory>();
		for (Object[] obj101 : listaa) {

			String reqtitle = (String) obj101[0];
			String firstname = ((String) obj101[1]);
			Date comdate = (Date) (obj101[2]);	
			
			System.out.println("===============");
			String comdesc = (String ) (obj101[3]);	
			System.out.println("----------------");


						
			CommentsHistory che = new CommentsHistory();
			che.setRequesttitle(reqtitle);
			che.setUserfirstname(firstname);
			che.setCommentsdesc(comdesc);
			che.setCreateddate(comdate);
			System.out.println(che);
			allCommentHistory.add(che);
		}

		
		return allCommentHistory;
	}
	
	/* ========= No of raised Request for Department=============== */
	public int noOfRaisedRequestInDepartment(int uid) {
		return this.requestRepo.noOfRaisedRequestInDepartment(uid);
	}


	/* ========= No of Complete Request for Department=============== */
	public int noOfAssignedRequestInDepartment(int uid) {
		return this.requestRepo.noOfAssignedRequestInDepartment(uid);
	}




}
