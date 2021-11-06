package com.crts.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crts.entity.CommentsHistory;
import com.crts.entity.DeptEntity;
import com.crts.entity.MailResponse;
import com.crts.entity.RequestEntity;
import com.crts.entity.ResponseDept;
import com.crts.entity.StatusEntity;
import com.crts.entity.StatusEntityview;
import com.crts.entity.UserDeptEntity;
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

@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
				responseHeaders.add("authorization", this.jwtUtilHelper.generateToken(userDetails));
				responseHeaders.add("userrole", this.userDeptService.IsUserAdmin(isValidUser.getuId()));
				responseHeaders.add("Access-Control-Expose-Headers", "authorization");
				responseHeaders.add("Access-Control-Expose-Headers", "userrole");
				ar.setObj(lstue);
				ar.setStatus(HttpStatus.OK);
				ar.setMessage("Login Successfully");
				return ResponseEntity.ok().headers(responseHeaders).body(ar);
			} else {
				ar.setObj(null);
				ar.setStatus(HttpStatus.UNAUTHORIZED);
				ar.setMessage("Login Failed, Unauthorized user");
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
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public ResponseEntity<Apiresponse> savenewuser(@RequestBody UserEntity userEntity, HttpSession session,
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
					ar.setMessage("Something went wrong");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				ar.setObj(null);
				ar.setStatus(HttpStatus.BAD_REQUEST);
				ar.setMessage("Something went wrong");
				return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
			}
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("Something went wrong");
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
	@GetMapping("/getuser/{uName}")
	public ResponseEntity<Apiresponse> GetUserByUname(@PathVariable("uName") String uName, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equals("supadmin") || userrole.equals("admin")) {
			UserEntity user = this.userService.validatingUserNameOrEmailid(uName);
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

	/* ======== Update user date PROCESS ======== */
	@PostMapping("/updateusers")
	public ResponseEntity<Apiresponse> updateUser(@RequestBody UserEntity userEntity, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {

		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		UserEntity oldUserData = this.userService.validatingUserNameOrEmailid(userEntity.getuEmail());
		userEntity.setuId(oldUserData.getuId());
		userEntity.setuCBy(oldUserData.getuCBy());
		userEntity.setuCDate(oldUserData.getuCDate());
		if (userEntity.getuPassword() == null || userEntity.getuPassword().isEmpty()) {
			userEntity.setuPassword(oldUserData.getuPassword());
		}
		UserEntity updateUserData = this.userService.CreateNewUser(userEntity);

		if (updateUserData != null) {
			lstue.add(updateUserData);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("UpdateUser Sucessfully");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
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
				ar.setMessage("Something Went wrong");
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

	
	@GetMapping("/getalldeptpermissionforuser/{uid}")
	public List<UserDeptEntity> getAllDeptpermission(@PathVariable("uid") int uid) {
		List<UserDeptEntity> list = this.userDeptService.getAllRollByUserId(uid);
		return list;
	}
	
	
	
	/* ======== Save list of Department user role ======== */
	@PostMapping(path = "/saveuserrole")
	public ResponseEntity<Apiresponse> SaveUserDeptRole(@RequestBody List<UserDeptEntity> userDeptEntity,
			HttpServletRequest request, HttpSession session, HttpServletResponse response) {

		System.out.println(userDeptEntity);

		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

//		int DUDA = this.userDeptService.DeleteUserDeptAccess((int) session.getAttribute("userId"));

		UserDeptEntity entity = null;
		entity = this.userDeptService.saveUserDeptAccess(userDeptEntity, userid);

		lstue.add(null);
		ar.setObj(lstue);
		ar.setStatus(HttpStatus.OK);
		ar.setMessage("Password reset !! New Password has been sent to the provided Email Address");
		return ResponseEntity.ok().body(ar);
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
			List<DeptEntity> allAdminDepartment = this.deptService.getAllDept();
			lstue.add(allAdminDepartment);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Select Department for Super Admin Successfully");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equals("admin")) {

			List<DeptEntity> allAdminDepartment = this.userDeptService.AllAdminDepartment(userid);
			lstue.add(allAdminDepartment);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Select Department for Admin Successfully");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.UNAUTHORIZED);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== Save Department PROCESS ===== */
	@PostMapping("/createdept")
	public ResponseEntity<Apiresponse> savedept(@RequestBody DeptEntity deptEntity, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) {

		System.out.println();
		System.out.println(deptEntity);
		System.out.println();

		Apiresponse ar = new Apiresponse();
		DeptEntity isDepartmentSave = new DeptEntity();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		deptEntity.setDecreatedby(userid);

		if (userrole.equals("supadmin")) {
			boolean flagcheck = false;
			List<DeptEntity> list = this.deptService.getAllDept();

			for (DeptEntity alldept : list) {
				if (alldept.getDecode().equalsIgnoreCase(deptEntity.getDepcode())) {
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
					ar.setMessage("Something went wrong");
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
					ar.setMessage("Something went wrong");
					return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
				}
			} else {
				System.out.println("3");
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
				ar.setMessage("Something went wrong");
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

	/* ===== GET ALL DEPARTMENT CODE LIST ========= */
	@GetMapping("/getalldepartmentcodelist")
	public ResponseEntity<Apiresponse> getAllDepartmentCodeList(HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		List<String> deptCodeList = this.deptService.getAllDeptCode();
		if (deptCodeList != null) {
			lstue.add(deptCodeList);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("gettting the Department code List Successfully !!");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== Get User By DeptID ===== */
	@GetMapping("/getuserbydeptcode/{deptcode}")
	public ResponseEntity<Apiresponse> getUserByDeptID(@PathVariable("deptcode") String deptcode,
			HttpServletRequest request) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();

		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		int a = this.deptService.getDeptIDByDeptCode(deptcode);
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
		lstue.add(ue);
		ar.setObj(lstue);
		ar.setStatus(HttpStatus.OK);
		ar.setMessage("gettting the User List Successfully !!");
		return ResponseEntity.ok().body(ar);

	}

	/* ===== Get Dept By Deptcode ===== */
	@GetMapping("/getdeptbydeptcode/{deptcode}")
	public ResponseEntity<Apiresponse> getDeptByDeptCode(@PathVariable("deptcode") String deptcode,
			HttpServletRequest request) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		DeptEntity deptEntity = this.deptService.getDeptByDeptCode(deptcode);		
		lstue.add(deptEntity);
		ar.setObj(lstue);
		ar.setStatus(HttpStatus.OK);
		ar.setMessage("gettting the User List Successfully !!");
		return ResponseEntity.ok().body(ar);
	}

	/* ===== Generate New Request ===== */
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
			ar.setMessage("Something went wrong,Please try again !!");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	
	
	/* ===== VIEW REQUEST PAGE Super admin allarrisestatus ========= */
	@GetMapping("/viewallrequest/allarrisestatus")
	public ResponseEntity<Apiresponse> viewallreqallarrisestatus(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allarrisestatus = this.statusService.getAllRaisedLastUpdateRequestforsuperadmin();
			lstue.add(allarrisestatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllRaisedClosedRequestforadmin(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE Super admin allassignstatus ========= */
	@GetMapping("/viewallrequest/allassignstatus")
	public ResponseEntity<Apiresponse> viewallreqallassignstatus(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allassignstatus = this.statusService.getAllAssignLastUpdateRequestforsuperadmin();
			lstue.add(allassignstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService
					.getAllAssignLastUpdateRequestforadmin(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE Super admin allarriseclosedstatus ========= */
	@GetMapping("/viewallrequest/allarriseclosedstatus")
	public ResponseEntity<Apiresponse> viewallreqallarriseclosedstatus(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllRaisedClosedRequestforsuperadmin();
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService
					.getAllRaisedLastUpdateRequestforadmin(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE user allarrisestatus ========= */
	@GetMapping("/viewallrequest/allraisestatususer")
	public ResponseEntity<Apiresponse> viewallallarrisestatususers(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allraisestatususer = this.statusService.getAllArrisedLastUpdateRequest(userid);
			lstue.add(allraisestatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allraisestatususer = this.statusService.getAllArrisedLastUpdateRequest(userid);
			lstue.add(allraisestatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("user")) {
			List<StatusEntityview> allraisestatususer = this.statusService.getAllArrisedLastUpdateRequest(userid);
			lstue.add(allraisestatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		}

		else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE user allassignstatus ========= */
	@GetMapping("/viewallrequest/allassignstatussususer")
	public ResponseEntity<Apiresponse> viewallallassignstatususers(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allassignstatususer = this.statusService.getAllAssignLastUpdateRequest(userid);
			lstue.add(allassignstatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allassignstatususer = this.statusService.getAllAssignLastUpdateRequest(userid);
			lstue.add(allassignstatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("user")) {
			List<StatusEntityview> allassignstatususer = this.statusService.getAllAssignLastUpdateRequest(userid);
			lstue.add(allassignstatususer);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		}

		else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST PAGE user allarriseclosedstatus ========= */
	@GetMapping("/viewallrequest/allarriseclosedstatususer")
	public ResponseEntity<Apiresponse> viewallarriseclosedstatususers(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		if (userrole.equalsIgnoreCase("supadmin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Super Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("admin")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		} else if (userrole.equalsIgnoreCase("user")) {
			List<StatusEntityview> allarriseclosedstatus = this.statusService.getAllArrisedClosedRequest(userid);
			lstue.add(allarriseclosedstatus);
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Admin all request View");
			return ResponseEntity.ok().body(ar);
		}

		else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== VIEW REQUEST BY REQUEST CODE PAGE ========= */
	@GetMapping("getrequestbycode/{rcode}")
	public ResponseEntity<Apiresponse> GetRequestByreqcode(@PathVariable("rcode") String rcode,
			HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		UserEntity RequestSenderPerson = this.userService.getById(userid);

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

	
	
	
	
	
	
	
	/* ===== MODIFY REQUEST PROCESS========= */
	@PostMapping("/updaterequest")
	public ResponseEntity<Apiresponse> updateRequest(@RequestBody RequestEntity requestEntity,
			HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		StatusEntity statusEntity = new StatusEntity();
		statusEntity.setSestdesc(requestEntity.getStatusEntity().get(0).getSestdesc());

		RequestEntity updateRequest = this.requestService.updateRequest(requestEntity, statusEntity, userid);
		if (updateRequest != null) {
			lstue.add(updateRequest);
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

	/* ===== Get REQUEST Comment History========= */
	@GetMapping("getrequestallhistory/{reqcode}")
	public ResponseEntity<Apiresponse> getRequestAllHistory(@PathVariable("reqcode") String reqcode,
			HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));
		List<CommentsHistory> allCommentHistory = this.requestService.getAllCommentByReqId(reqcode);
		for (CommentsHistory ch : allCommentHistory) {
			lstue.add(ch);
		}
		if (allCommentHistory != null) {
			ar.setObj(lstue);
			ar.setStatus(HttpStatus.OK);
			ar.setMessage("Request History View");
			return ResponseEntity.ok().body(ar);
		} else {
			ar.setObj(null);
			ar.setStatus(HttpStatus.BAD_REQUEST);
			ar.setMessage("Something went wrong in comment history");
			return new ResponseEntity<Apiresponse>(ar, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== Dashboard Data========= */
	@GetMapping("/dashboarddata")
	public ResponseEntity<Apiresponse> updateRequest(Model model, HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {
		Apiresponse ar = new Apiresponse();
		ArrayList<Object> lstue = new ArrayList<Object>();
		String userrole = request.getHeader("userrole");
		int userid = Integer.parseInt(request.getHeader("userid"));

		int noOfDepartment = this.userDeptService.noOfDepartment(userid);
		int noOfUserInDepartment = this.userDeptService.noOfUserInDepartment(userid);
		int noOfAssignedRequestInDepartment = this.requestService.noOfAssignedRequestInDepartment(userid);
		int noOfRaisedRequestInDepartment = this.requestService.noOfRaisedRequestInDepartment(userid);

		model.addAttribute("noOfDepartment", noOfDepartment);
		model.addAttribute("noOfUserInDepartment", noOfUserInDepartment);
		model.addAttribute("noOfAssignedRequestInDepartment", noOfAssignedRequestInDepartment);
		model.addAttribute("noOfRaisedRequestInDepartment", noOfRaisedRequestInDepartment);

		lstue.add(model);
		ar.setObj(lstue);
		ar.setStatus(HttpStatus.OK);
		ar.setMessage("Request History View");
		return ResponseEntity.ok().body(ar);
	}
}