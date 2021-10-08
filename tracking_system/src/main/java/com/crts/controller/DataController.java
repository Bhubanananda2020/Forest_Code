package com.crts.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crts.entity.CommentsHistory;
import com.crts.entity.DeptEntity;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;
import com.crts.entity.StatusEntityview;
import com.crts.entity.UserEntity;
import com.crts.service.DeptService;
import com.crts.service.RequestService;
import com.crts.service.StatusService;
import com.crts.service.UserDeptService;
import com.crts.service.UserService;

@RestController
@RequestMapping("/getdata")
@CrossOrigin("*")
public class DataController {

	@Autowired
	private DeptService deptService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDeptService userDeptService;

	/* ===== Get Email Id By Firstname ===== */
	@GetMapping("/getuseridbyusername")
	public String getUserEmailIdByUserName(@RequestParam("deptcode") String deptcode, Model model) {
		return this.userService.getEmailIdByFirstName(deptcode);

	}

	/* ===== Get User By DeptID ===== */
	@GetMapping("/getuserbydeptid")
	public List<UserEntity> getDeptIDByDeptCode(@RequestParam("deptcode") String deptcode, Model model) {
		int a = this.deptService.getDeptIDByDeptCode(deptcode);
		System.out.println(a);
		List<Object[]> list = this.userDeptService.getAllUserByDeptid(a);

		List<UserEntity> ue = new ArrayList<UserEntity>();

		for (Object[] obj : list) {

			int id = (int) obj[0];
			String name = (String) obj[1];
			String role = ((String) obj[2]);

			if (!role.equalsIgnoreCase("no permission")) {
				UserEntity ue1 = new UserEntity(id, name);
				ue.add(ue1);
			}
		}

		System.out.println(ue);
		return ue;
	}

	/*
	 * @GetMapping("/getallstatucreateby") public List<StatusEntity>
	 * getAllStatusCreateBy(@RequestParam("reqcreateby") String reqcreateby, Model
	 * model) { return this.statusService.getAllCreateBy(reqcreateby); }
	 */
	/*
	 * @GetMapping("/getalldeptuserid") public List<UserDeptEntity>
	 * getDeptByUserId(@RequestParam("uid") String uid1, Model model) { int uid =
	 * Integer.parseInt(uid1); return this.userDeptService.getAllDeptByuserid(uid);
	 * }
	 * 
	 */

	/*
	 * ===================================
	 */
	@GetMapping("/getdeptuser")
	public List<DeptEntity> getDeptUserByDeptCode(@RequestParam("deptcode") String deptcode, Model model) {
		return this.deptService.getDeptUserByDeptcode(deptcode);
	}

	@GetMapping("/getalldept")
	public List<DeptEntity> getAllDept(@RequestParam("deptcode") String deptcode, Model model) {
		return this.deptService.getDeptUserByDeptcode(deptcode);
	}

	/* ======== Get Request By Request Code ======== */
	@GetMapping("/getrequestbycode")
	public RequestEntity GetRequestByreqcode(@RequestParam("rcode") String rcode) {
		RequestEntity requestByReqcode = this.requestService.getRequestByReqcode(rcode);
		return requestByReqcode;
	}

	/* ======== Get Status By Request Code ======== */
	@GetMapping("/getstatusbycode")
	public StatusEntity GetStatusByreqcode(@RequestParam("rcode") int rcode) {
		StatusEntity statusByReqcode = this.statusService.getStatusByRequestNumber(rcode);
		return statusByReqcode;
	}

	/*
	 * ======== Save list of Department user role ========
	 * 
	 * @PostMapping(path="/saveuserrole") public String
	 * SaveUserDeptRole(@RequestBody List<UserDeptEntity> userDeptEntity) { String
	 * res = null; System.out.println(userDeptEntity); if (userDeptEntity != null) {
	 * this.userDeptService.saveUserDeptAccess(userDeptEntity); } return
	 * "redirect:/home/user/getalluser"; }
	 */
	/*
	 * ======== Get ALL LATEST Request By LOGIN USER ID ========
	 * 
	 * @GetMapping("/getalllatestrequestrequest") public List<Object[]>
	 * getAllLastUpdateRequest(@RequestParam("uid") int uid) { List<Object[]> list =
	 * this.statusService.getAllArrisedLastUpdateRequest(uid); return list; }
	 * 
	 * ===== Get All comments History By Request Id and user id =====
	 * 
	 * @GetMapping("/getallcommenthistoy") public List<CommentsHistory>
	 * getAllComment(String reqnum) {
	 * 
	 * List<Object[]> listaa = this.requestService.getAllCommentByReqId(reqnum);
	 * 
	 * List<CommentsHistory> allhistory = new ArrayList<CommentsHistory>(); for
	 * (Object[] obj101 : listaa) { String reqtitle = (String) obj101[0]; String
	 * firstname = ((String) obj101[1]);
	 * 
	 * String comdesc = (String) (obj101[2]);
	 * 
	 * CommentsHistory che = new CommentsHistory(); che.setRequesttitle(reqtitle);
	 * che.setUserfirstname(firstname); che.setCommentsdesc(comdesc);
	 * System.out.println(che); allhistory.add(che); } return allhistory; }
	 */
	@GetMapping("/getalldeptpermission")
	public String getAllDeptpermission(@RequestParam("uid") int uid, @RequestParam("deptid") int deptid) {
		System.out.println(this.userDeptService.getAllDeptpermission(uid, deptid));

		return this.userDeptService.getAllDeptpermission(uid, deptid);
	}
	
	@GetMapping("/getalladminviewreq")
	public List<StatusEntityview> getAllRaisedLastUpdateRequestforadmin(@RequestParam("uid") int uid) {
		System.out.println(this.statusService.getAllRaisedLastUpdateRequestforadmin(uid));

		return this.statusService.getAllRaisedLastUpdateRequestforadmin(uid);
	}
		
	
	@GetMapping("/getAll")
	public List<UserEntity> getAllUsers() {
		return this.userService.getAllUser();
	}
	


}
