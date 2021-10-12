package com.crts.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crts.config.AssignRequestPDFExporter;
import com.crts.config.ClosedRequestPDFExporter;
import com.crts.config.RequestPDFExporter;
import com.crts.config.UserPDFExporter;
import com.crts.dto.DepartmentDTO;
import com.crts.dto.RequestDTO;
import com.crts.dto.UserDTO;
import com.crts.dto.UserDeptDTO;
import com.crts.entity.CommentsEntity;
import com.crts.entity.CommentsHistory;
import com.crts.entity.DeptEntity;
import com.crts.entity.MailResponse;
import com.crts.entity.RequestEntity;
import com.crts.entity.StatusEntity;
import com.crts.entity.StatusEntityview;
import com.crts.entity.UserDeptEntity;
import com.crts.entity.UserEntity;
import com.crts.helper.Message;
import com.crts.service.CommentsService;
import com.crts.service.DeptService;
import com.crts.service.RequestService;
import com.crts.service.StatusService;
import com.crts.service.UserDeptService;
import com.crts.service.UserService;
import com.crts.serviceimpl.EmailService;

@RestController
@RequestMapping("/home")
@CrossOrigin("*")
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
	private UserDeptService userDeptService;

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		session.setAttribute("message", new Message("Successfully Logout!!", "alert-danger"));
		session.invalidate();
		return new ResponseEntity<String>("Logout Successfully", HttpStatus.OK);
	}

	/* ===== HOME PAGE ========= */
	@GetMapping("user/dashboard")
	public ResponseEntity<Model> dashboard(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			model.addAttribute("title", "Home - Request Tracking System");
			int noOfDepartment = this.userDeptService.noOfDepartment((int) session.getAttribute("uid"));
			int noOfUserInDepartment = this.userDeptService.noOfUserInDepartment((int) session.getAttribute("uid"));
			int noOfAssignedRequestInDepartment = this.requestService
					.noOfAssignedRequestInDepartment((int) session.getAttribute("uid"));
			int noOfRaisedRequestInDepartment = this.requestService
					.noOfRaisedRequestInDepartment((int) session.getAttribute("uid"));
			model.addAttribute("noofdept", noOfDepartment);
			model.addAttribute("noofuser", noOfUserInDepartment);
			model.addAttribute("noofassignreq", noOfAssignedRequestInDepartment);
			model.addAttribute("noofraisedreq", noOfRaisedRequestInDepartment);
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== NEW USER PAGE ========= */
	@GetMapping("createuser")
	public ResponseEntity<Model> createuser(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			model.addAttribute("title", "Add User - Request Tracking System");
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== Get ALL USER VIEW PAGE ========= */
	@GetMapping("getuser")
	public ResponseEntity<UserDTO> viewalluser(@ModelAttribute("userEntity") UserEntity userEntity,
			BindingResult bindingResult, Model model, HttpSession session, HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			ud.setMessage("You are not a valid User!!");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			if (session.getAttribute("isUserAdmin").equals("supadmin")) {
				List<UserEntity> alluser = this.userService.getAllUser();
				ud.setLstuserEntity(alluser);
				ud.setMessage("You are super admin User!!");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
			} else {
				List<UserEntity> alluser = this.userService
						.getAllUserByuIdForAdminView((int) session.getAttribute("uid"));
				ud.setLstuserEntity(alluser);
				ud.setMessage("You are admin User!!");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
			}
		}
	}

	/* ===== GET USER BY ID PAGE ========= */
	@GetMapping("{uname}/getuser")
	public ResponseEntity<UserDTO> GetUserByUname(@PathVariable("uname") String uname, Model model, HttpSession session,
			HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			ud.setMessage("You are not a valid User!!");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			UserEntity user = this.userService.validatingUserNameOrEmailid(uname);
			session.setAttribute("userId", user.getuId());
			session.setAttribute("userFname", user.getuFName());
			session.setAttribute("userfullname", user.getuFName() + " " + user.getuLName());
			session.setAttribute("usercurrpass", user.getuPassword());
			session.setAttribute("oldcreby", user.getuCBy());
			session.setAttribute("oldcredt", user.getuCDate());
			ud.setUserEntity(user);
			ud.setMessage("Success!!");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);

		}
	}

	/* ===== UPDATE PASSWORD PAGE ========= */
	@GetMapping("updatepassword")
	public ResponseEntity<Model> validuserfrom(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			model.addAttribute("title", "Update Password - Request Tracking System");
			model.addAttribute("useremail", session.getAttribute("useremail"));
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== Reset PASSWORD PAGE ========= */
	@GetMapping("mypassword")
	public ResponseEntity<Model> updtLoginUserPswd(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			model.addAttribute("title", "Reset Password - Request Tracking System");
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== USER ACCESS PAGE ========= */
	@GetMapping("{userId}/getuseraccess")
	public ResponseEntity<Model> GetUserDeptAccess(@PathVariable("userId") int userId, Model model, HttpSession session,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		System.out.println(userId);
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			List<UserDeptEntity> allRollByUserId = this.userDeptService.getAllRollByUserId(userId);
			if (allRollByUserId.isEmpty()) {
				String flag = "true";
				model.addAttribute("flag", flag);
			} else {
				String flag = "False";
				model.addAttribute("flag", flag);
			}
			List<DeptEntity> deptCodeList = this.deptService.getAllDept();
			model.addAttribute("deptCodeList", deptCodeList);
			model.addAttribute("allRollByUserId", allRollByUserId);

			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);
			session.setAttribute("curdate", date);
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== MY PROFILE PAGE ========= */
	@GetMapping("myprofile")
	public ResponseEntity<UserDTO> myprofilepage(Model model, HttpSession session, HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		ud.setUserEntity((UserEntity) session.getAttribute("isValidUser"));
		return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
	}

	/* ===== SELECT DEPARTMENT PAGE ========= */
	@GetMapping("selectdepartment")
	public ResponseEntity<Model> selectdepartment(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			if (session.getAttribute("isUserAdmin").equals("admin")) {
				model.addAttribute("title", "Select Department - Request Tracking System");
				List<String> allAdminDepartment = this.userDeptService
						.AllAdminDepartment((int) session.getAttribute("uid"));
				model.addAttribute("allAdminDepartment", allAdminDepartment);
				return new ResponseEntity<Model>(model, HttpStatus.OK);
			} else if (session.getAttribute("isUserAdmin").equals("supadmin")) {
				System.out.println("sup add");
				model.addAttribute("title", "Select Department - Request Tracking System");
				List<String> allAdminDepartment = this.userDeptService.AllSupAdminDepartment();
				model.addAttribute("allAdminDepartment", allAdminDepartment);
				return new ResponseEntity<Model>(model, HttpStatus.OK);
			} else {
				model.addAttribute("message", "You are not a valid User!!");
				return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
			}
		}
	}

	/* ===== ADD DEPARTMENT PAGE ========= */
	@GetMapping("{depcode}/createdepartment")
	public ResponseEntity<Model> createdepartment(@PathVariable("depcode") String depcode, Model model,
			HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			model.addAttribute("title", "Child Department - Request Tracking System");
			model.addAttribute("depcode", depcode);
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== VIEW ALL DEPARTMENT PAGE ========= */
	@GetMapping("getalldepartmentlist")
	public ResponseEntity<DepartmentDTO> GetAllDepartment(HttpSession session, HttpServletResponse response) {
		DepartmentDTO dd = new DepartmentDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			dd.setMessage("You are not a valid User!!");
			return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.UNAUTHORIZED);
		} else {
			if (session.getAttribute("isUserAdmin").equals("admin")) {
				List<DeptEntity> allDept = this.deptService.getAllDeptAdmin((int) session.getAttribute("uid"));
				dd.setLstdeptEntity(allDept);
				return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.OK);
			} else {
				List<DeptEntity> allDept = this.deptService.getAllDept();
				dd.setLstdeptEntity(allDept);
				return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.OK);
			}
		}
	}

	/* ===== New REQUEST PAGE ========= */
	@GetMapping("user/createrequest")
	public ResponseEntity<Model> addrequestpage(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			List<String> deptCodeList = this.deptService.getAllDeptCode();
			model.addAttribute("deptCodeList", deptCodeList);
			model.addAttribute("title", "Add Request - Request Tracking System");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		}
	}

	/* ===== VIEW REQUEST PAGE ========= */
	@GetMapping("user/viewrequest")
	public ResponseEntity<Model> viewallreq(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			List<StatusEntityview> allarrisestatus = this.statusService
					.getAllArrisedLastUpdateRequest((int) session.getAttribute("uid"));
			List<StatusEntityview> allassignstatus = this.statusService
					.getAllAssignLastUpdateRequest((int) session.getAttribute("uid"));
			List<StatusEntityview> allarriseclosedstatus = this.statusService
					.getAllArrisedClosedRequest((int) session.getAttribute("uid"));

			model.addAttribute("title", "View All - Request Track	ing System");
			model.addAttribute("allassignstatus", allassignstatus);
			model.addAttribute("allarrisestatus", allarrisestatus);
			model.addAttribute("allarriseclosedstatus", allarriseclosedstatus);
			return new ResponseEntity<Model>(model, HttpStatus.OK);
		}
	}

	/* ===== VIEW ALL REQUEST FOR ADMIN OR SUPER ADMIN PAGE ========= */
	@GetMapping("user/adminviewrequest")
	public ResponseEntity<Model> adminviewallreq(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			System.out.println(session.getAttribute("isUserAdmin"));
			if (session.getAttribute("isUserAdmin").equals("admin")) {
				List<StatusEntityview> allraisestatusadmin = this.statusService
						.getAllRaisedLastUpdateRequestforadmin((int) session.getAttribute("uid"));
				List<StatusEntityview> allassignstatusadmin = this.statusService
						.getAllAssignLastUpdateRequestforadmin((int) session.getAttribute("uid"));
				List<StatusEntityview> allarriseclosedstatusadmin = this.statusService
						.getAllRaisedClosedRequestforadmin((int) session.getAttribute("uid"));
				model.addAttribute("title", "Admin View - Request Track	ing System");
				model.addAttribute("allraisestatusadmin", allraisestatusadmin);
				model.addAttribute("allassignstatusadmin", allassignstatusadmin);
				model.addAttribute("allarriseclosedstatusadmin", allarriseclosedstatusadmin);
				return new ResponseEntity<Model>(model, HttpStatus.OK);
			} else {
				List<StatusEntityview> allraisestatusadmin = this.statusService
						.getAllRaisedLastUpdateRequestforsuperadmin();
				List<StatusEntityview> allassignstatusadmin = this.statusService
						.getAllAssignLastUpdateRequestforsuperadmin();
				List<StatusEntityview> allarriseclosedstatusadmin = this.statusService
						.getAllRaisedClosedRequestforsuperadmin();
				model.addAttribute("title", "Admin View - Request Track	ing System");
				model.addAttribute("allraisestatusadmin", allraisestatusadmin);
				model.addAttribute("allassignstatusadmin", allassignstatusadmin);
				model.addAttribute("allarriseclosedstatusadmin", allarriseclosedstatusadmin);
				return new ResponseEntity<Model>(model, HttpStatus.OK);
			}
		}
	}

	/* ===== VIEW REQUEST BY REQUEST CODE PAGE ========= */
	@GetMapping("{rcode}/getrequestbycode")
	public ResponseEntity<Model> GetRequestByreqcode(@PathVariable("rcode") String rcode,
			@ModelAttribute("requestEntity") RequestEntity requestEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		System.out.println(rcode);
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			model.addAttribute("message", "You are not a valid User!!");
			return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
		} else {
			RequestEntity getReuestEntity = this.requestService.getRequestByReqcode(rcode);
			if (session.getAttribute("uid").equals(getReuestEntity.getReqassignto())
					|| session.getAttribute("uid").equals(getReuestEntity.getRecreatedby())) {

				System.out.println("++++INSIDE REQUEST EDIT+++++++");
				model.addAttribute("requestEntity", getReuestEntity);
				session.setAttribute("assigndate", getReuestEntity.getReqassigndate());
				session.setAttribute("prevassigndepartmentcode", getReuestEntity.getReqdeptcode());
				session.setAttribute("reqinicomment1", getReuestEntity.getReqinicomment());
				session.setAttribute("reqcreateby", getReuestEntity.getRecreatedby());
				session.setAttribute("reqprevid", getReuestEntity.getReqid());
				List<String> deptCodeList = this.deptService.getAllDeptCode();
				model.addAttribute("deptCodeList", deptCodeList);
				UserEntity byId = this.userService.getById(getReuestEntity.getReqassignto());
				model.addAttribute("prevusedata", byId.getuFName());
				List<CommentsHistory> allCommentHistory = this.requestService.getAllCommentByReqId(rcode);
				StatusEntity stusentity = this.statusService.getStatusByRequestNumber(getReuestEntity.getReqid());
				String prevstdesc = stusentity.getSestdesc();
				model.addAttribute("prevstdesc", prevstdesc);
				model.addAttribute("allCommentHistory", allCommentHistory);
				return new ResponseEntity<Model>(model, HttpStatus.OK);
			} else {
				model.addAttribute("message", "You are not a valid User!!");
				return new ResponseEntity<Model>(model, HttpStatus.UNAUTHORIZED);
			}

		}
	}

	/* ===== lOGIN USER BY USER NAME AND PASSWORD PROCESS ========= */
	@PostMapping(value = "/loginProcess")
	public ResponseEntity<UserDTO> checklogin(@Valid UserEntity userEntity, BindingResult bindingResult, Model model,
			Errors errors, @RequestParam("uName") String username, @RequestParam("uPassword") String password,
			HttpSession session, HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		try {
			if (errors.hasErrors()) {
				ud.setMessage("Invalid Data!!");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
			} else {
				UserEntity isValidUser = this.userService.userValidate(username, password);
				if (isValidUser != null) {
					String isUserAdmin = this.userDeptService.IsUserAdmin(isValidUser.getuId());
					ud.setUserEntity(isValidUser);
					ud.setMessage("Welcome " + isValidUser.getuFName());
					ud.setRole(isUserAdmin);

					session.setAttribute("isValidUser", isValidUser);
					session.setAttribute("username", isValidUser.getuFName());
					session.setAttribute("useremail", isValidUser.getuEmail());
					session.setAttribute("uid", isValidUser.getuId());
					session.setAttribute("isUserAdmin", isUserAdmin);

					return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
				} else {
					ud.setMessage("Invalid UserId or Password");
					return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
				}
			}
		} catch (Exception e) {
			ud.setMessage("Invalid data");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
		}
	}

	/* ===== Save New User PROCESS ===== */
	@PostMapping("/createuser")
	public ResponseEntity<UserDTO> savenewuser(@Valid @RequestBody UserEntity userEntity, Errors errors,
			HttpSession session, HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			ud.setMessage("Unauthorized User");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				if (errors.hasErrors()) {
					ud.setMessage("Invalid Data !!");
					return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
				} else {
					long millis = System.currentTimeMillis();
					java.sql.Date date = new java.sql.Date(millis);
					Boolean userstatus = Boolean.valueOf(userEntity.isuIsActive());
					userEntity.setuCDate(date);
					userEntity.setuCBy((String) session.getAttribute("username"));
					userEntity.setuIsActive(userstatus);
					UserEntity isUserSave = this.userService.CreateNewUser(userEntity);
					if (isUserSave != null) {
						Map<String, Object> model = new HashMap<>();
						model.put("UserCreateBy", session.getAttribute("username"));
						model.put("NewUser", isUserSave.getuName());
						model.put("NewPassword", userEntity.getuPassword());
						service.sendNewUserEmail(isUserSave.getuEmail(), model);
						ud.setUserEntity(isUserSave);
						ud.setMessage("User Create Successfully !! New Username is  :  " + isUserSave.getuName());
						return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
					} else {
						ud.setMessage("Something went wrong, Please try again");
						return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
					}
				}
			} catch (Exception e) {
				ud.setMessage("Something went wrong, Please try again");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/* ======== Update user date PROCESS ======== */
	@PostMapping("/updateusersprocess")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserEntity userEntity, Errors errors, Model model,
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			ud.setMessage("Unauthorized User");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
			userEntity.setuId((int) session.getAttribute("userId"));
			userEntity.setuCBy((String) session.getAttribute("oldcreby"));
			userEntity.setuCDate((Date) session.getAttribute("oldcredt"));
			if (userEntity.getuPassword() == null || userEntity.getuPassword().isEmpty()) {
				userEntity.setuPassword((String) session.getAttribute("usercurrpass"));
			}
			UserEntity entity = this.userService.CreateNewUser(userEntity);
			session.removeAttribute((String) session.getAttribute("userId").toString());
			session.removeAttribute((String) session.getAttribute("userFname"));
			session.removeAttribute((String) session.getAttribute("userfullname"));
			session.removeAttribute((String) session.getAttribute("usercurrpass"));
			session.removeAttribute((String) session.getAttribute("oldcreby").toString());
			session.removeAttribute((String) session.getAttribute("oldcredt").toString());
			ud.setUserEntity(entity);
			ud.setMessage("User data update Successfully!! :  ");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
		}
	}

	/* ===== CHANGING PASSWORD PROCESS========= */
	@PostMapping("/updatepwProcess")
	public ResponseEntity<UserDTO> changepw(@Valid UserEntity userEntity, BindingResult bindingResult, Errors errors,
			@RequestParam("uPassword") String password, @RequestParam("uName") String username, HttpSession session,
			HttpServletResponse response) {
		UserDTO ud = new UserDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			ud.setMessage("Unauthorized User");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				if (errors.hasErrors()) {
					ud.setMessage("Invalid input");
					return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
				} else {
					UserEntity ue = new UserEntity();
					ue = this.userService.validatingUserNameOrEmailid(username);
					if (ue != null) {
						ue.setuPassword(password);
						boolean isPasswordUpdate = this.userService.updatePassword(ue);
						if (isPasswordUpdate) {
							ud.setUserEntity(ue);
							ud.setMessage("Password update Succesfully!!");
							return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
						} else {
							ud.setMessage("Invalid Email Address!!");
							return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
						}
					} else {
						errors.hasErrors();
						ud.setMessage("Invalid Email Address!!");
						return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
					}
				}
			} catch (Exception e) {
				ud.setMessage("Invalid Email Address!!");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/* ===== RESET PASSWORD PROCESS========= */
	@PostMapping("/resetpasswordprocess")
	public ResponseEntity<UserDTO> resetpw(@RequestParam("uName") String username, HttpSession session,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		UserDTO ud = new UserDTO();
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			ud.setMessage("Unauthorized User");
			return new ResponseEntity<UserDTO>(ud, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				UserEntity ue = new UserEntity();
				ue = this.userService.validatingUserNameOrEmailid(username);
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
						ud.setMessage("Password reset !! New Password has been sent to the provided Email Address");
						ud.setUserEntity(ue);
						return new ResponseEntity<UserDTO>(ud, HttpStatus.OK);
					} else {
						ud.setMessage("Password not update !!");
						return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
					}
				} else {
					ud.setMessage("Password not update !!Invalid Username or Email Address");
					return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				ud.setMessage("Password not update !!Something went worng, Please try again");
				return new ResponseEntity<UserDTO>(ud, HttpStatus.BAD_REQUEST);
			}
		}

	}

	/* ===== Save Department PROCESS ===== */
	@PostMapping("/savedeptprocess")
	public ResponseEntity<DepartmentDTO> savedept(@Valid DeptEntity deptEntity, Errors errors,
			@RequestParam("decode") String dcode, @RequestParam("dename") String dname,
			@RequestParam("depcode") String dpcode, @RequestParam("deisactive") String diact, HttpSession session,
			HttpServletResponse response) {
		DepartmentDTO dd = new DepartmentDTO();
		DeptEntity isDepartmentSave = new DeptEntity();
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			dd.setMessage("Unauthorized User");
			return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				int createby = (int) session.getAttribute("uid");
				if (errors.hasErrors()) {
					System.out.println(errors.toString());
					dd.setMessage("Invalid Data, Department Not Save !!");
					return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
				} else {
					if (session.getAttribute("isUserAdmin").equals("supadmin")) {
						boolean flagcheck = false;
						List<String> list = this.userDeptService.AllSupAdminDepartment();
						for (String alldept : list) {
							if (alldept.equalsIgnoreCase(dpcode)) {
								flagcheck = true;
							}
						}
						if (flagcheck) {
							isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby, diact);
							if (isDepartmentSave != null) {
								dd.setMessage("Department Save Successfully !!");
								dd.setDeptEntity(isDepartmentSave);
								return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.OK);
							} else {
								dd.setMessage("Invalid Data, Department Not Save !!");
								return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
							}
						} else if (dcode.equalsIgnoreCase(dpcode)) {
							isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby, diact);
							if (isDepartmentSave != null) {
								dd.setMessage("Department Save Successfully !!");
								dd.setDeptEntity(isDepartmentSave);
								return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.OK);
							} else {
								dd.setMessage("Invalid Data, Department Not Save !!");
								return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
							}
						} else {
							dd.setMessage("Invalid Data, Department Not Save !!");
							return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
						}
					} else {
						isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby, diact);
						if (isDepartmentSave != null) {
							dd.setMessage("Department Save Successfully !!");
							dd.setDeptEntity(isDepartmentSave);
							return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.OK);
						} else {
							dd.setMessage("Invalid Data, Department Not Save !!");
							return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				dd.setMessage("Invalid Data, Department Not Save !!");
				return new ResponseEntity<DepartmentDTO>(dd, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/* ===== Generate New Request PROCESS ===== */
	@PostMapping("/generaterequest")
	public ResponseEntity<RequestDTO> saveRequest(@Valid RequestEntity requestEntity, Errors errors,
			@RequestParam("reqtitle") String reqtitle, @RequestParam("reqdesc") String reqdesc,
			@RequestParam("piority") int piority, @RequestParam("severity") int severity,
			@RequestParam("reqdeptcode") String reqtodepart, @RequestParam("reqassignto") int reqtoperson,
			@RequestParam("reqinicomment") String reqfstcomment, HttpSession session, HttpServletResponse response) {
		RequestDTO rd = new RequestDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			rd.setMessage("Unauthorized User");
			return new ResponseEntity<RequestDTO>(rd, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				if (errors.hasErrors()) {
					rd.setMessage("Invalid input");
					return new ResponseEntity<RequestDTO>(rd, HttpStatus.BAD_REQUEST);
				} else {
					int createby = (int) session.getAttribute("uid");
					String getNewRequestNum1 = this.requestService.getLastRequestNumberByDeptId(reqtodepart);
					String getNewRequestNum = reqtodepart + getNewRequestNum1;
					RequestEntity isRequestSave = this.requestService.saveRequest(reqtitle, reqdesc, getNewRequestNum,
							reqtodepart, reqtoperson, reqfstcomment, createby, piority, severity);
					if (isRequestSave != null) {
						UserEntity uedata = this.userService.getById(reqtoperson);
						String RequestPersonEmailId = uedata.getuEmail();
						Map<String, Object> model = new HashMap<>();
						model.put("Requestby", session.getAttribute("username"));
						model.put("reqno", getNewRequestNum);
						model.put("reqtitle", reqtitle);
						model.put("reqmessage", reqdesc);
						System.out.println(RequestPersonEmailId);
						service.sendRequestEmail(RequestPersonEmailId, model);
						rd.setRequestEntity(isRequestSave);
						rd.setMessage(
								"Request Generate Successfully !! Your Request Refernece No : " + getNewRequestNum);
						return new ResponseEntity<RequestDTO>(rd, HttpStatus.OK);
					} else {
						rd.setMessage("Request Generate Fail !! Please try Againg");
						return new ResponseEntity<RequestDTO>(rd, HttpStatus.BAD_REQUEST);
					}
				}
			} catch (Exception e) {
				rd.setMessage("Request Generate Fail !! Please try Againg");
				return new ResponseEntity<RequestDTO>(rd, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/* ===== MODIFY REQUEST PROCESS========= */
	@PostMapping("/updaterequest")
	public ResponseEntity<RequestDTO> updateRequest(@Valid RequestEntity requestEntity, Errors errors,
			@RequestParam("reqid") int reqid, @RequestParam("reqcode") String reqcode,
			@RequestParam("reqtitle") String reqtitle, @RequestParam("piority") int piority,
			@RequestParam("severity") int severity, @RequestParam("reqdesc") String reqdesc,
			@RequestParam("reqdeptcode") String reqtodepart, @RequestParam("reqassignto") int reqtoperson,
			@RequestParam("reqinicomment") String reqfstcomment, @RequestParam("sestdesc") String reqstatus,
			HttpSession session, HttpServletResponse response) {

		RequestDTO rd = new RequestDTO();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			rd.setMessage("Unauthorized User");
			return new ResponseEntity<RequestDTO>(rd, HttpStatus.UNAUTHORIZED);
		} else {
			try {
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);
				char stuscod = reqstatus.charAt(0);
				if (errors.hasErrors()) {
					rd.setMessage("Invalid Data");
					return new ResponseEntity<RequestDTO>(rd, HttpStatus.UNAUTHORIZED);
				} else {
					CommentsEntity ce = new CommentsEntity();
					ce.setCmdesc(reqfstcomment);
					ce.setCmreqdate(date);
					ce.setCmreqcreateby((int) session.getAttribute("uid"));
					StatusEntity se = new StatusEntity();
					se.setSescode(stuscod);
					se.setSestdesc(reqstatus);
					se.setReqdate(date);
					se.setReqcreateby((int) session.getAttribute("uid"));
					RequestEntity re = new RequestEntity();
					List<StatusEntity> selist = new ArrayList<StatusEntity>();
					List<CommentsEntity> celist = new ArrayList<CommentsEntity>();
					re.setReqid((int) session.getAttribute("reqprevid"));
					re.setReqdeptcode(reqtodepart);

					if (reqtodepart.equalsIgnoreCase((String) session.getAttribute("prevassigndepartmentcode"))) {
						re.setReqcode(reqcode);
					} else {
						String getNewRequestNum1 = this.requestService.getLastRequestNumberByDeptId(reqtodepart);
						String getNewRequestNum = reqtodepart + getNewRequestNum1;
						re.setReqcode(getNewRequestNum);
					}
					re.setReqtitle(reqtitle);
					re.setReqdesc(reqdesc);
					re.setPiority(piority);
					re.setSeverity(severity);
					re.setReqassigndate((Date) session.getAttribute("assigndate"));
					re.setReqassignto(reqtoperson);
					re.setReqinicomment((String) session.getAttribute("reqinicomment1"));
					re.setRecreatedby((int) session.getAttribute("reqcreateby"));
					selist.add(se);
					celist.add(ce);
					re.setStatusEntity(selist);
					re.setcCommentsEntity(celist);
					se.setRequestEntity(re);
					ce.setRequestEntity(re);
					RequestEntity entity = this.requestService.updateRequest(re);
					if (entity != null) {
						if (reqstatus.equals("CLOSE REQUEST")) {
							UserEntity byId = this.userService.getById((int) session.getAttribute("reqcreateby"));
							Map<String, Object> model = new HashMap<>();
							model.put("Requestby", session.getAttribute("username"));
							model.put("reqno", reqcode);
							model.put("reqtitle", reqtitle);
							model.put("reqmessage", "Your request is closed");
							MailResponse emailw = service.sendResetPasswordEmail(byId.getuEmail(), model);
							System.out.println("Mail sent " + byId.getuEmail());
						}
						rd.setRequestEntity(entity);
						rd.setMessage("Request Update Successfully !!" + entity.getReqcode());
						return new ResponseEntity<RequestDTO>(rd, HttpStatus.OK);
					} else {
						rd.setMessage("Request Update Fail !! Please try Againg ");
						return new ResponseEntity<RequestDTO>(rd, HttpStatus.NOT_MODIFIED);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				rd.setMessage("Invalid Data Or Data Not Save !!");
				return new ResponseEntity<RequestDTO>(rd, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/* ======== Save list of Department user role ======== */
	@PostMapping(path = "/saveuserrole")
	public ResponseEntity<UserDeptDTO> SaveUserDeptRole(@RequestBody List<UserDeptEntity> userDeptEntity,
			HttpSession session, HttpServletResponse response) {
		UserDeptDTO udd = new UserDeptDTO();
		if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			udd.setMessage("Unauthorized User");
			return new ResponseEntity<UserDeptDTO>(udd, HttpStatus.UNAUTHORIZED);
		} else {
			System.out.println((int) session.getAttribute("userId"));
			int duda = this.userDeptService.DeleteUserDeptAccess((int) session.getAttribute("userId"));
			System.out.println(duda);
			UserDeptEntity entity = null;
			entity = this.userDeptService.saveUserDeptAccess(userDeptEntity);
			if (duda > 0) {
			}
			if (entity != null) {
				session.removeAttribute("userId");
				udd.setUserDeptEntity(entity);
				udd.setMessage("User Role Successfully Save For!! :  " + entity.getUserid());
				return new ResponseEntity<UserDeptDTO>(udd, HttpStatus.OK);
			} else {
				session.removeAttribute("userId");
				udd.setMessage("User Role in not Save !! " + entity.getUserid());
				return new ResponseEntity<UserDeptDTO>(udd, HttpStatus.OK);
			}
		}
	}

	/* ======== user data pdf generation process ======== */
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<UserEntity> listUsers = this.userService.getAllUser();

		UserPDFExporter exporter = new UserPDFExporter(listUsers);
		exporter.export(response);

	}

	/* ======== request raise pdf generation process ======== */
	@GetMapping("user/viewrequest/export/pdf")
	public void exportToPDF1(HttpServletResponse response, HttpSession session) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=raise request_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<StatusEntityview> allraisestatus = this.statusService
				.getAllArrisedLastUpdateRequest((int) session.getAttribute("uid"));

		RequestPDFExporter exporter = new RequestPDFExporter(allraisestatus);
		exporter.export(response);

	}

	/* ======== assigned request pdf generation process ======== */
	@GetMapping("user/viewrequest/export1/pdf")
	public void exportToPDF2(HttpServletResponse response, HttpSession session) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=assigned request_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<StatusEntityview> allassignstatus = this.statusService
				.getAllAssignLastUpdateRequest((int) session.getAttribute("uid"));

		AssignRequestPDFExporter exporter = new AssignRequestPDFExporter(allassignstatus);
		exporter.export(response);

	}

	/* ======== closed request pdf generation process ======== */
	@GetMapping("user/viewrequest/export2/pdf")
	public void exportToPDF3(HttpServletResponse response, HttpSession session) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=closed request_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<StatusEntityview> allarriseclosedstatus = this.statusService
				.getAllArrisedClosedRequest((int) session.getAttribute("uid"));

		ClosedRequestPDFExporter exporter = new ClosedRequestPDFExporter(allarriseclosedstatus);
		exporter.export(response);

	}

}