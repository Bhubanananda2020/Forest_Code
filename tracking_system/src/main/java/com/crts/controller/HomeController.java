package com.crts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crts.dto.RequestDTO;
import com.crts.entity.DeptEntity;
import com.crts.entity.MailResponse;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntityview;
import com.crts.entity.UserEntity;
import com.crts.helper.JwtUtilHelper;
import com.crts.response.Apiresponse;
import com.crts.service.DeptService;
import com.crts.service.RequestService;
import com.crts.service.StatusService;
import com.crts.service.UserDeptService;
import com.crts.service.UserService;
import com.crts.serviceimpl.CustomUserDetailsService;
import com.crts.serviceimpl.EmailService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private EmailService service;

	@Autowired
	private JwtUtilHelper jwtUtilHelper;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private UserDeptService userDeptService;

	/* ===== lOGIN USER BY USER NAME AND PASSWORD PROCESS ========= */
	@RequestMapping(value = "/userauthentication", method = RequestMethod.POST)
	public ResponseEntity<Apiresponse> checklogin(@RequestBody UserEntity userEntity, HttpSession session) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		try {
			UserEntity isValidUser = this.userService.userValidate(userEntity.getuName(), userEntity.getuPassword());
			lstue.add(isValidUser);

			if (isValidUser != null) {
				UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userEntity.getuName());
				responseHeaders.add("Authorization", "Bearer " + this.jwtUtilHelper.generateToken(userDetails));
				responseHeaders.add("userrole", this.userDeptService.IsUserAdmin(isValidUser.getuId()));
				responseHeaders.add("", "");
				ar.setObj(lstue);
				ar.setStatus(HttpStatus.OK);
				ar.setMessage("Login Successfully");
				return ResponseEntity.ok().headers(responseHeaders).body(ar);
			} else {
				ar.setObj(null);
				ar.setStatus(HttpStatus.UNAUTHORIZED);
				ar.setMessage("Login Fail, Unauthorized user");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something Went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== Save New User PROCESS ===== */
	@PostMapping("/createuser")
	public ResponseEntity<Apiresponse> savenewuser(@Valid @RequestBody UserEntity userEntity, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equals("supadmin") || userrole.equals("admin")) {
			try {
				UserEntity udata = this.userService.getById(userid);
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);
				Boolean userstatus = Boolean.valueOf(userEntity.isuIsActive());
				userEntity.setuCDate(date);
				userEntity.setuCBy(udata.getuFName());

				userEntity.setuIsActive(userstatus);
				UserEntity isUserSave = this.userService.CreateNewUser(userEntity);
				lstue.add(isUserSave);
				if (isUserSave != null) {
					Map<String, Object> model = new HashMap<>();
					model.put("UserCreateBy", udata.getuFName());
					model.put("NewUser", isUserSave.getuName());
					model.put("NewPassword", userEntity.getuPassword());
					service.sendNewUserEmail(isUserSave.getuEmail(), model);
					ar.setObj(lstue);
					ar.setStatus(HttpStatus.OK);
					ar.setMessage("User Create Successfully");
					return ResponseEntity.ok().body(ar);
				} else {
					ar.setObj(null);
					ar.setStatus(HttpStatus.BAD_REQUEST);
					ar.setMessage("Something went wronge");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Something went wronge");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("Something went wronge");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== Get ALL USER VIEW PAGE ========= */
	@GetMapping("/getalluser")
	public ResponseEntity<Apiresponse> viewalluser(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");

		int userid = Integer.parseInt(request.getHeader("userid"));
		UserEntity RequestSenderPerson = this.userService.getById(userid);

		if (userrole.equals("supadmin")) {
			System.out.println("this is super admin");
			List<UserEntity> alluser = this.userService.getAllUser();
			System.out.println(alluser);
			lstue.add(alluser);

			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("super admin All User view");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equals("admin")) {
			System.out.println("this is admin");
			List<UserEntity> alluser = this.userService.getAllUserByuIdForAdminView(userid);
			lstue.add(alluser);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("super admin All User view");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== GET USER BY ID PAGE ========= */
	@GetMapping("/{uname}/getuser")
	public ResponseEntity<Apiresponse> GetUserByUname(@PathVariable("uname") String uname, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equals("supadmin") || userrole.equals("admin")) {
			UserEntity user = this.userService.validatingUserNameOrEmailid(uname);
			lstue.add(user);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Get Single User Sucessfully");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== UPDATE PASSWORD PAGE ========= */
	@GetMapping("/updatepassword")
	public ResponseEntity<Apiresponse> validuserfrom(@RequestBody UserEntity userEntity, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		UserEntity ue = this.userService.validatingUserNameOrEmailid(userEntity.getuEmail());
		if (ue != null) {
			ue.setuPassword(userEntity.getuPassword());
			boolean isPasswordUpdate = this.userService.updatePassword(ue);

			if (isPasswordUpdate) {
				lstue.add(ue);
				ar.setObj(lstue);
				ar.setStatus(HttpStatus.OK);
				ar.setMessage("Password Update Successfully");
				return ResponseEntity.ok().body(ar);
			} else {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Something Went wronge");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Invalid Email Address!!");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== Reset PASSWORD PAGE ========= */
	@GetMapping("/resetpassword")
	public ResponseEntity<Apiresponse> updtLoginUserPswd(@RequestBody UserEntity userEntity, HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equals("supadmin") || userrole.equals("admin")) {
			try {
				UserEntity ue = this.userService.validatingUserNameOrEmailid(userEntity.getuEmail());
				if (ue != null) {
					String newPassword = this.userService.getNewPassword();
					ue.setuPassword(newPassword);
					boolean isPasswordUpdate = this.userService.updatePassword(ue);
					if (isPasswordUpdate) {
						Map<String, Object> model = new HashMap<>();
						model.put("Requestby", session.getAttribute("username"));
						model.put("reqtitle", "Your password is reset");
						model.put("reqmessage", newPassword);
						MailResponse emailw = service.sendResetPasswordEmail(ue.getuEmail(), model);
						lstue.add(ue);
						ar.setObj(lstue);
						ar.setStatus(HttpStatus.OK);
						ar.setMessage("Password reset !! New Password has been sent to the provided Email Address");
						return ResponseEntity.ok().body(ar);
					} else {
						ar.setObj(null);
						ar.setStatus(HttpStatus.BAD_REQUEST);
						ar.setMessage("Password not update !!");
						return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
					}
				} else {
					ar.setObj(null);
					ar.setStatus(HttpStatus.BAD_REQUEST);
					ar.setMessage("Password not update !!Invalid Username or Email Address !!");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Password not update !! Something went worng, Please try again  !!");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}

	}

	/* ===== SELECT DEPARTMENT PAGE ========= */
	@GetMapping("/selectdepartment")
	public ResponseEntity<Apiresponse> selectdepartment(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {

		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		if (userrole.equals("supadmin")) {
			List<String> allAdminDepartment = this.userDeptService.AllSupAdminDepartment();
			lstue.add(allAdminDepartment);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Select Department for Super Admin Successfully");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equals("admin")) {
			List<String> allAdminDepartment = this.userDeptService.AllAdminDepartment(userid);
			lstue.add(allAdminDepartment);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Select Department for Admin Successfully");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("Something went wronge");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== Save Department PROCESS ===== */
	@PostMapping("/createdept")
	public ResponseEntity<Apiresponse> savedept(@RequestBody DeptEntity deptEntity, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {

		Apiresponse ar = new Apiresponse();
		DeptEntity isDepartmentSave = new DeptEntity();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		deptEntity.setDecreatedby(userid);

		if (userrole.equals("supadmin")) {
			boolean flagcheck = false;
			List<String> list = this.userDeptService.AllSupAdminDepartment();
			for (String alldept : list) {
				if (alldept.equalsIgnoreCase(deptEntity.getDepcode())) {
					flagcheck = true;
				}
			}
			if (flagcheck) {
				isDepartmentSave = this.deptService.saveDepartment(deptEntity);
				if (isDepartmentSave != null) {
					lstue.add(isDepartmentSave);
					ar.setObj(lstue);
					ar.setStatus(HttpStatus.OK);
					ar.setMessage("Department Create Successfully");
					return ResponseEntity.ok().body(ar);
				} else {
					ar.setObj(null);
					ar.setStatus(HttpStatus.BAD_REQUEST);
					ar.setMessage("Something went wronge");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} else if (deptEntity.getDecode().equalsIgnoreCase(deptEntity.getDepcode())) {
				isDepartmentSave = this.deptService.saveDepartment(deptEntity);
				if (isDepartmentSave != null) {
					lstue.add(isDepartmentSave);
					ar.setObj(lstue);
					ar.setStatus(HttpStatus.OK);
					ar.setMessage("Department Create Successfully");
					return ResponseEntity.ok().body(ar);
				} else {
					ar.setObj(null);
					ar.setStatus(HttpStatus.BAD_REQUEST);
					ar.setMessage("Something went wronge");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} else {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Invalid Data, Department Not Save !!");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else if (userrole.equals("admin")) {
			isDepartmentSave = this.deptService.saveDepartment(deptEntity);
			if (isDepartmentSave != null) {
				lstue.add(isDepartmentSave);
				ar.setObj(lstue);
				ar.setStatus(HttpStatus.OK);
				ar.setMessage("Department Create Successfully");
				return ResponseEntity.ok().body(ar);
			} else {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Something went wronge");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== VIEW ALL DEPARTMENT PAGE ========= */
	@GetMapping("/getalldepartmentlist")
	public ResponseEntity<Apiresponse> GetAllDepartment(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equals("supadmin")) {
			List<DeptEntity> allDept = this.deptService.getAllDept();
			lstue.add(allDept);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin Department View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equals("admin")) {
			List<DeptEntity> allDept = this.deptService.getAllDeptAdmin(userid);
			lstue.add(allDept);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin Department View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== Generate New Request PROCESS ===== */
	@PostMapping("/generaterequest")
	public ResponseEntity<Apiresponse> saveRequest(@RequestBody RequestEntity requestEntity, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		String getNewRequestNum = requestEntity.getReqdeptcode()
				+ this.requestService.getLastRequestNumberByDeptId(requestEntity.getReqdeptcode());
		requestEntity.setRecreatedby(userid);
		requestEntity.setReqcode(getNewRequestNum);

		RequestEntity isRequestSave = this.requestService.saveRequest(requestEntity);
		if (isRequestSave != null) {
			UserEntity uedata = this.userService.getById(requestEntity.getReqassignto());
			String RequestPersonEmailId = uedata.getuEmail();
			UserEntity RequestSenderPerson = this.userService.getById(userid);
			Map<String, Object> model = new HashMap<>();
			model.put("Requestby", RequestSenderPerson.getuFName());
			model.put("reqno", getNewRequestNum);
			model.put("reqtitle", requestEntity.getReqtitle());
			model.put("reqmessage", requestEntity.getReqdesc());
			System.out.println(RequestPersonEmailId);
			service.sendRequestEmail(RequestPersonEmailId, model);
			lstue.add(isRequestSave);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Request Generate Successfully !! Your Request Refernece No : " + getNewRequestNum);
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wronge");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE ========= */
	@GetMapping("/viewallrequest")
	public ResponseEntity<Apiresponse> viewallreq(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {

		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		if (userrole.equalsIgnoreCase("supadmin")) {

			List<StatusEntityview> allarrisestatus = this.statusService.getAllArrisedLastUpdateRequest(userid);
			List<StatusEntityview> allassignstatus = this.statusService.getAllAssignLastUpdateRequest(userid);
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarrisestatus);
			lstue.add(allassignstatus);
			lstue.add(allarriseclosedstatus);

			List<StatusEntityview> allraisestatusadmin = this.statusService
					.getAllRaisedLastUpdateRequestforsuperadmin();
			List<StatusEntityview> allassignstatusadmin = this.statusService
					.getAllAssignLastUpdateRequestforsuperadmin();
			List<StatusEntityview> allarriseclosedstatusadmin = this.statusService
					.getAllRaisedClosedRequestforsuperadmin();
			lstue.add(allraisestatusadmin);
			lstue.add(allassignstatusadmin);
			lstue.add(allarriseclosedstatusadmin);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {

			List<StatusEntityview> allarrisestatus = this.statusService.getAllArrisedLastUpdateRequest(userid);
			List<StatusEntityview> allassignstatus = this.statusService.getAllAssignLastUpdateRequest(userid);
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarrisestatus);
			lstue.add(allassignstatus);
			lstue.add(allarriseclosedstatus);

			List<StatusEntityview> allraisestatusadmin = this.statusService
					.getAllRaisedLastUpdateRequestforadmin(userid);
			List<StatusEntityview> allassignstatusadmin = this.statusService
					.getAllAssignLastUpdateRequestforadmin(userid);
			List<StatusEntityview> allarriseclosedstatusadmin = this.statusService
					.getAllRaisedClosedRequestforadmin(userid);
			lstue.add(allraisestatusadmin);
			lstue.add(allassignstatusadmin);
			lstue.add(allarriseclosedstatusadmin);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);

		} else if (userrole.equalsIgnoreCase("user")) {
			List<StatusEntityview> allarrisestatus = this.statusService.getAllArrisedLastUpdateRequest(userid);
			List<StatusEntityview> allassignstatus = this.statusService.getAllAssignLastUpdateRequest(userid);
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarrisestatus);
			lstue.add(allassignstatus);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin Department View");
			return ResponseEntity.ok().body(ar);

		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wronge");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST BY REQUEST CODE PAGE ========= */
	@GetMapping("{rcode}/getrequestbycode")
	public ResponseEntity<Apiresponse> GetRequestByreqcode(@PathVariable("rcode") String rcode,
			HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		System.out.println(rcode);
		RequestEntity getReuestEntity = this.requestService.getRequestByReqcode(rcode);

		if ((userid == getReuestEntity.getReqassignto()) || (userid == getReuestEntity.getRecreatedby())
				|| (userrole.equalsIgnoreCase("admin")) || (userrole.equalsIgnoreCase("supadmin"))) {
			System.out.println("++++INSIDE REQUEST EDIT+++++++");
			lstue.add(getReuestEntity);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("You are not a valid User");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}
}
