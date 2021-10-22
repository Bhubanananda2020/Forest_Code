package com.crts.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crts.dto.RequestDTO;
import com.crts.entity.CommentsEntity;
import com.crts.entity.CommentsHistory;
import com.crts.entity.MailResponse;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;
import com.crts.entity.UserEntity;
import com.crts.repo.RequestRepo;
import com.crts.service.RequestService;
import com.crts.service.UserService;

@Service
public class RequestServiceImpl implements RequestService {

	@Autowired
	private UserService userService;

	@Autowired
	private RequestRepo requestRepo;
	@Autowired
	private RequestService requestService;
	@Autowired
	private EmailService service;

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
	public RequestEntity saveRequest(RequestEntity requestEntity) {
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
			se.setReqcreateby(requestEntity.getRecreatedby());
			selist.add(se);
			requestEntity.setReqassigndate(date);
			requestEntity.setStatusEntity(selist);
			se.setRequestEntity(requestEntity);

			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(requestEntity);
			isSaveRequest = this.requestRepo.save(requestEntity);
			return isSaveRequest;
		} catch (Exception e) {
			System.out.println(e);
			return isSaveRequest = null;
		}
	}

	/* ======== Get Request By Request Code ======== */
	public RequestEntity getRequestByReqcode(String rcode) {
		return this.requestRepo.getByreqcode(rcode);
	}

	/* ======== Update Request ======== */
	@Transactional
	public RequestEntity updateRequest(RequestEntity re, StatusEntity se, int userid) {
		RequestEntity rereturn = new RequestEntity();
		try {
			RequestEntity oldRequestEntity = this.requestService.getRequestByReqcode(re.getReqcode());

			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			CommentsEntity ce = new CommentsEntity();
			ce.setCmdesc(re.getReqinicomment());
			ce.setCmreqdate(date);
			ce.setCmreqcreateby(userid);
			se.setSescode(se.getSestdesc().charAt(0));
			se.setReqdate(date);
			se.setReqcreateby(userid);
			List<StatusEntity> selist = new ArrayList<StatusEntity>();
			List<CommentsEntity> celist = new ArrayList<CommentsEntity>();
			selist.add(se);
			celist.add(ce);
			if (re.getReqdeptcode().equalsIgnoreCase(oldRequestEntity.getReqdeptcode())) {
				re.setReqcode(re.getReqcode());
			} else {
				String getNewRequestNum = re.getReqdeptcode()
						+ this.requestService.getLastRequestNumberByDeptId(re.getReqdeptcode());
				re.setReqcode(getNewRequestNum);
			}
			re.setReqassigndate(oldRequestEntity.getReqassigndate());
			re.setReqinicomment(oldRequestEntity.getReqinicomment());
			re.setRecreatedby(oldRequestEntity.getRecreatedby());
			re.setStatusEntity(selist);
			re.setcCommentsEntity(celist);
			se.setRequestEntity(re);
			ce.setRequestEntity(re);

			rereturn = this.requestRepo.saveAndFlush(re);
			if (rereturn != null) {
				if (se.getSestdesc().equals("CLOSE REQUEST")) {
					UserEntity senderId = this.userService.getById(oldRequestEntity.getRecreatedby());
					Map<String, Object> model = new HashMap<>();
					model.put("Requestby", senderId.getuFName());
					model.put("reqno", re.getReqcode());
					model.put("reqtitle", re.getReqtitle());
					model.put("reqmessage", "Your request is closed");
					MailResponse emailw = service.sendResetPasswordEmail(senderId.getuEmail(), model);
					System.out.println("Mail sent " + senderId.getuEmail());
				}
				return rereturn;
			}
			else
			{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


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
			String comdesc = (String) (obj101[3]);
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
