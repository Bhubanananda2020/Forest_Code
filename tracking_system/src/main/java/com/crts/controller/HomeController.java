package com.crts.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crts.config.AssignRequestPDFExporter;
import com.crts.config.ClosedRequestPDFExporter;
import com.crts.config.RequestPDFExporter;
import com.crts.config.UserPDFExporter;
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

@Controller
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

	@Autowired
	private CommentsService commentsService;

	@RequestMapping("/error")
	public String error(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		return "error";
	}

	/* ===== LOGIN PAGE OR WELCOME PAGE ========= */
	@RequestMapping("/")
	public String welcome(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		session.invalidate();
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		model.addAttribute("title", "Login - Request Tracking System");
		return "index";
	}

	/* ===== LOGIN PAGE OR WELCOME PAGE ========= */
	@RequestMapping("/index")
	public String welcomepage(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		session.invalidate();
		model.addAttribute("title", "Login - Request Tracking System");
		return "redirect:/home/";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		session.setAttribute("message", new Message("Successfully Logout!!", "alert-danger"));
		session.invalidate();
		return "redirect:/home/";
	}

	/* ===== HOME PAGE ========= */
	@RequestMapping("user/dashboard")
	public String dashboard(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");

		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
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

			return "user/dashboardpage";
		}
	}

	/* ===== NEW USER PAGE ========= */
	@RequestMapping("user/createuser")
	public String createuser(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");

		System.out.println(session.getAttribute("isUserAdmin"));

		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			model.addAttribute("title", "Add User - Request Tracking System");
			return "user/createuser";
		}
	}

	/* ===== MODIFY USER PAGE ========= */
	@GetMapping("{uname}/getuserbyuname")
	public String GetUserByUname(@PathVariable("uname") String uname,
			@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			UserEntity user = this.userService.validatingUserNameOrEmailid(uname);
			model.addAttribute("title", "Edit User - Request Tracking System");
			model.addAttribute("userEntity", user);
			session.setAttribute("userId", user.getuId());
			session.setAttribute("userFname", user.getuFName());
			session.setAttribute("userfullname", user.getuFName() + " " + user.getuLName());
			session.setAttribute("usercurrpass", user.getuPassword());
			session.setAttribute("oldcreby", user.getuCBy());
			session.setAttribute("oldcredt", user.getuCDate());

			return "user/edituser";
		}
	}

	/* ===== UPDATE PASSWORD PAGE ========= */
	@RequestMapping("user/updatepassword")
	public String validuserfrom(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else {
			model.addAttribute("title", "Update Password - Request Tracking System");
			return "user/updatepassword";
		}
	}

	/* ===== Reset PASSWORD PAGE ========= */
	@RequestMapping("user/mypassword")
	public String updtLoginUserPswd(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			model.addAttribute("title", "Reset Password - Request Tracking System");
			return "user/resetpassword";
		}
	}

	/* ===== Get ALL USER VIEW PAGE ========= */
	@RequestMapping("user/getalluser")
	public String viewalluser(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {

			if (session.getAttribute("isUserAdmin").equals("supadmin")) {
				List<UserEntity> alluser = this.userService.getAllUser();
				model.addAttribute("title", "Home - Request Tracking System");
				model.addAttribute("alluser", alluser);
				return "user/view_userall";
			} else {
				List<UserEntity> alluser = this.userService
						.getAllUserByuIdForAdminView((int) session.getAttribute("uid"));
				model.addAttribute("title", "Home - Request Tracking System");
				model.addAttribute("alluser", alluser);
				return "user/view_userall";
			}

		}
	}

	/* ===== USER ACCESS PAGE ========= */
	@GetMapping("user/getuseraccess")
	public String GetUserDeptAccess(@ModelAttribute("userDeptEntity") UserDeptEntity userDeptEntity,
			BindingResult bindingResult, Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			List<UserDeptEntity> allRollByUserId = this.userDeptService
					.getAllRollByUserId((int) session.getAttribute("userId"));

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
			return "user/user_dept_access";
		}
	}

	/* ===== MY PROFILE PAGE ========= */
	@RequestMapping("user/myprofile")
	public String myprofilepage(Model model, HttpSession session, HttpServletResponse response) {
		model.addAttribute("title", "My Profile - Request Tracking System");
		model.addAttribute("ue", session.getAttribute("isValidUser"));
		return "user/myprofile";
	}

	/* ===== SELECT DEPARTMENT PAGE ========= */
	@RequestMapping("user/selectdepartment")
	public String selectdepartment(@ModelAttribute("deptEntity") DeptEntity deptEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			if (session.getAttribute("isUserAdmin").equals("admin")) {
				model.addAttribute("title", "Select Department - Request Tracking System");
				List<String> allAdminDepartment = this.userDeptService
						.AllAdminDepartment((int) session.getAttribute("uid"));
				model.addAttribute("allAdminDepartment", allAdminDepartment);
				session.setAttribute("allAdminDepartment", allAdminDepartment);
				return "user/listofdepartment";
			} else if (session.getAttribute("isUserAdmin").equals("supadmin")) {
				System.out.println("sup add");
				model.addAttribute("title", "Select Department - Request Tracking System");
				List<String> allAdminDepartment = this.userDeptService.AllSupAdminDepartment();
				model.addAttribute("allAdminDepartment", allAdminDepartment);
				session.setAttribute("allAdminDepartment", allAdminDepartment);
				return "user/selectdepartmentsup";
			} else {
				session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
				return "user/dashboardpage";
			}

		}

	}

	/* ===== ADD DEPARTMENT PAGE ========= */
	@RequestMapping("user/createdepartment")
	public String createdepartment(@ModelAttribute("deptEntity") DeptEntity deptEntity, BindingResult bindingResult,
			@RequestParam("depcode") String depcode, Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {

			model.addAttribute("title", "Child Department - Request Tracking System");
			session.setAttribute("depcode", depcode);
			return "user/createdepartment";
		}
	}

	/* ===== VIEW ALL DEPARTMENT PAGE ========= */
	@GetMapping("user/getalldepartmentlist")
	public String GetAllDepartment(@ModelAttribute("deptEntity") DeptEntity deptEntity, BindingResult bindingResult,
			Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {

			if (session.getAttribute("isUserAdmin").equals("admin")) {
				List<DeptEntity> allDept = this.deptService.getAllDeptAdmin((int) session.getAttribute("uid"));
				model.addAttribute("allDept", allDept);
				return "user/view_alldepartment";
			} else {
				List<DeptEntity> allDept = this.deptService.getAllDept();
				model.addAttribute("allDept", allDept);
				return "user/view_alldepartment";

			}
		}
	}

	/* ===== ADD REQUEST PAGE ========= */
	@RequestMapping("user/createrequest")
	public String addrequestpage(@ModelAttribute("requestEntity") RequestEntity requestEntity,
			BindingResult bindingResult, Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else {
			List<String> deptCodeList = this.deptService.getAllDeptCode();
			model.addAttribute("deptCodeList", deptCodeList);
			model.addAttribute("title", "Add Request - Request Tracking System");
			return "user/createrequest";
		}
	}

	/* ===== VIEW REQUEST PAGE ========= */
	@RequestMapping("user/viewrequest")
	public String viewallreq(Model model, HttpSession session, HttpServletResponse response) {

		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
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
			return "user/view_userrequest";
		}
	}

	/* ===== VIEW Admin or Super Admin REQUEST PAGE ========= */
	@RequestMapping("user/adminviewrequest")
	public String adminviewallreq(Model model, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");

		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else {

			System.out.println(session.getAttribute("isUserAdmin"));

			if (session.getAttribute("isUserAdmin").equals("admin")) {
				System.out.println();
				System.out.println("This For admin");
				System.out.println();
				System.out.println();
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
				System.out.println("===================");
				return "user/viewrequestadmin";
			}

			else {
				System.out.println();
				System.out.println("This For Super admin");
				System.out.println();
				System.out.println();
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
				System.out.println("===================");
				return "user/viewrequestadmin";
			}

		}
	}

	/* ===== MODIFY REQUEST PAGE ========= */
	@GetMapping("{rcode}/getrequestbycode")
	public String GetRequestByreqcode(@PathVariable("rcode") String rcode,
			@ModelAttribute("requestEntity") RequestEntity requestEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
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
				StatusEntity entity = this.statusService.getStatusByRequestNumber(getReuestEntity.getReqid());
				String prevstdesc = entity.getSestdesc();
				model.addAttribute("prevstdesc", prevstdesc);
				model.addAttribute("allCommentHistory", allCommentHistory);
				return "user/editrequest";
			} else {
				session.setAttribute("message", new Message("You are not a Valid User!!", "alert-danger"));
				return "redirect:/home/index";
			}

		}
	}

	/* ===== Add New Comment REQUEST Comment PAGE ========= */
	@GetMapping("{rcode}/getrequestbycodeforcomment")
	public String GetRequestByReqCodeForComment(@PathVariable("rcode") String rcode,
			@ModelAttribute("requestEntity") RequestEntity requestEntity, BindingResult bindingResult, Model model,
			HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/index";
		} else {
			RequestEntity getReuestEntity = this.requestService.getRequestByReqcode(rcode);
			model.addAttribute("requestEntity", getReuestEntity);
			String reqinicomment1 = getReuestEntity.getReqinicomment();
			session.setAttribute("reqinicomment1", reqinicomment1);
			List<String> deptCodeList = this.deptService.getAllDeptCode();
			model.addAttribute("deptCodeList", deptCodeList);
			return "user/commentrequest";
		}
	}

	/* ===== lOGIN USER BY USER NAME AND PASSWORD PROCESS ========= */
	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public String checklogin(@Valid UserEntity userEntity, BindingResult bindingResult, Model model, Errors errors,
			@RequestParam("uName") String username, @RequestParam("uPassword") String password, HttpSession session,
			HttpServletResponse response) {
		try {
			if (errors.hasErrors()) {
				session.setAttribute("message", new Message("Invalid UserId or Password!!", "alert-danger"));
				return "redirect:/home/";
			} else {
				UserEntity isValidUser = this.userService.userValidate(username, password);
				if (isValidUser != null) {
					session.setAttribute("isValidUser", isValidUser);
					session.setAttribute("username", isValidUser.getuFName());
					session.setAttribute("uid", isValidUser.getuId());
					String isUserAdmin = this.userDeptService.IsUserAdmin(isValidUser.getuId());
					session.setAttribute("isUserAdmin", isUserAdmin);
					return "redirect:/home/user/dashboard";
				} else {
					session.setAttribute("message", new Message("Invalid UserId or Password!!", "alert-danger"));
					return "/index";
				}
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Invalid UserId or Password !!", "alert-danger"));
			return "/index";
		}
	}

	/* ===== CHANGING PASSWORD PROCESS========= */
	@PostMapping("/updatepwProcess")
	public String changepw(@Valid UserEntity userEntity, BindingResult bindingResult, Errors errors,
			@RequestParam("uPassword") String password, @RequestParam("uName") String username, HttpSession session,
			HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			return "redirect:/home/";
		} else {
			try {
				if (errors.hasErrors()) {
					return "user/updatepassword";
				} else {
					UserEntity ue = new UserEntity();
					ue = this.userService.validatingUserNameOrEmailid(username);
					if (ue != null) {
						ue.setuPassword(password);
						boolean isPasswordUpdate = this.userService.updatePassword(ue);
						if (isPasswordUpdate) {
							session.setAttribute("message", new Message("Password update !!", "alert-success"));
							return "user/updatepassword";
						} else {
							session.setAttribute("message", new Message("Invalid Email Address !!", "alert-danger"));
							return "user/updatepassword";
						}
					} else {
						errors.hasErrors();
						session.setAttribute("message", new Message("Invalid Password !!", "alert-danger"));
						return "user/updatepassword";
					}
				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Invalid UserId or Password !!", "alert-danger"));
				errors.hasErrors();
				return "user/updatepassword";
			}
		}
	}

	/* ===== RESET PASSWORD PROCESS========= */
	@PostMapping("/resetpasswordprocess")
	public String resetpw(@RequestParam("uName") String username, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			try {
				UserEntity ue = new UserEntity();
				ue = this.userService.validatingUserNameOrEmailid(username);
				if (ue != null) {
					String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
					String specialCharacters = "!@#$";
					String numbers = "1234567890";
					String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
					Random random = new Random();
					char[] password = new char[8];
					password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
					password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
					password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
					password[3] = numbers.charAt(random.nextInt(numbers.length()));
					for (int i = 4; i < 8; i++) {
						password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
					}
					String newPassword = String.valueOf(password);
					ue.setuPassword(newPassword);
					boolean isPasswordUpdate = this.userService.updatePassword(ue);
					if (isPasswordUpdate) {
						Map<String, Object> model = new HashMap<>();
						model.put("Requestby", session.getAttribute("username"));
						model.put("reqtitle", "Your password is reset");
						model.put("reqmessage", newPassword);
						MailResponse emailw = service.sendResetPasswordEmail(ue.getuEmail(), model);
						session.setAttribute("message",
								new Message(
										"Password reset !! New Password has been sent to the provided Email Address",
										"alert-success"));
						return "redirect:/home/user/mypassword";
					} else {
						session.setAttribute("message", new Message("Password not update !!", "alert-danger"));
						return "user/mypassword";
					}
				} else {
					session.setAttribute("message", new Message("Invalid UserId or Email !!", "alert-danger"));
					return "user/mypassword";
				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Invalid UserId or Email !!", "alert-danger"));
				return "user/mypassword";
			}
		}

	}

	/* ===== Save Department PROCESS ===== */
	@PostMapping("/savedeptprocess")
	public String savedept(@Valid DeptEntity deptEntity, Errors errors, @RequestParam("decode") String dcode,
			@RequestParam("dename") String dname, @RequestParam("depcode") String dpcode,
			@RequestParam("deisactive") String diact, HttpSession session, HttpServletResponse response) {
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			try {
				int createby = (int) session.getAttribute("uid");
				if (errors.hasErrors()) {
					return "user/createdepartment";
				} else {

					if (session.getAttribute("isUserAdmin").equals("supadmin")) {
						boolean flagcheck = false;
						List<String> list = this.userDeptService.AllSupAdminDepartment();
						for (String alldept : list) {
							if (alldept.equalsIgnoreCase(dpcode)) {
								flagcheck = true;
							}
						}
System.out.println(flagcheck);
						if (flagcheck) {
							boolean isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby,
									diact);
							if (isDepartmentSave) {
								session.setAttribute("message",
										new Message("Department Save Successfully !!", "alert-success"));
								return "redirect:/home/user/getalldepartmentlist";
							} else {
								session.setAttribute("message",
										new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
								return "redirect:user/getalldepartmentlist";
							}
						} else if (dcode.equalsIgnoreCase(dpcode)) {
							boolean isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby,
									diact);
							if (isDepartmentSave) {
								session.setAttribute("message",
										new Message("Department Save Successfully !!", "alert-success"));
								return "redirect:/home/user/getalldepartmentlist";
							} else {
								session.setAttribute("message",
										new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
								return "redirect:user/getalldepartmentlist";
							}
						} else {
							session.setAttribute("message",
									new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
							return "redirect:user/getalldepartmentlist";
						}
					}

					else {
						boolean isDepartmentSave = this.deptService.saveDepartment(dcode, dname, dpcode, createby,
								diact);
						if (isDepartmentSave) {
							session.setAttribute("message",
									new Message("Department Save Successfully !!", "alert-success"));
							return "redirect:/home/user/getalldepartmentlist";
						} else {
							session.setAttribute("message",
									new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
							return "redirect:user/getalldepartmentlist";
						}

					}

				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
				errors.hasErrors();
				return "redirect:user/getalldepartmentlist";
			}
		}
	}

	/* ===== Generate New Request PROCESS ===== */
	@PostMapping("/generaterequest")
	public String saveRequest(@Valid RequestEntity requestEntity, Errors errors,
			@RequestParam("reqtitle") String reqtitle, @RequestParam("reqdesc") String reqdesc,
			@RequestParam("piority") int piority, @RequestParam("severity") int severity,
			@RequestParam("reqdeptcode") String reqtodepart, @RequestParam("reqassignto") int reqtoperson,
			@RequestParam("reqinicomment") String reqfstcomment, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else {
			try {
				if (errors.hasErrors()) {
					return "user/createrequest";
				} else {

					int createby = (int) session.getAttribute("uid");
					String getNewRequestNum1 = this.requestService.getLastRequestNumberByDeptId(reqtodepart);
					String getNewRequestNum = reqtodepart + getNewRequestNum1;

					boolean isRequestSave = this.requestService.saveRequest(reqtitle, reqdesc, getNewRequestNum,
							reqtodepart, reqtoperson, reqfstcomment, createby, piority, severity);
					if (isRequestSave) {
						session.setAttribute("message", new Message(
								"Request Generate Successfully !! Your Request Refernece No : " + getNewRequestNum,
								"alert-success"));
						UserEntity uedata = this.userService.getById(reqtoperson);
						String RequestPersonEmailId = uedata.getuEmail();
						Map<String, Object> model = new HashMap<>();
						model.put("Requestby", session.getAttribute("username"));
						model.put("reqno", getNewRequestNum);
						model.put("reqtitle", reqtitle);
						model.put("reqmessage", reqdesc);
						System.out.println(RequestPersonEmailId);
						service.sendRequestEmail(RequestPersonEmailId, model);
						return "redirect:user/createrequest";
					} else {
						session.setAttribute("message",
								new Message("Request Generate Fail !! Please try Againg", "alert-danger"));
						return "redirect:user/createrequest";
					}
				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
				errors.hasErrors();
				return "user/createrequest";
			}
		}
	}

	/* ===== MAIL SERVICE ========= */
	@PostMapping("/sendingEmail")
	public MailResponse sendEmail(@RequestParam("to") String to) {
		Map<String, Object> model = new HashMap<>();
		model.put("Name", "Admin");
		model.put("location", "odisha,India");

		return service.sendRequestEmail(to, model);

	}

	/* ===== MODIFY REQUEST PROCESS========= */
	@PostMapping("/updaterequest")
	public String updateRequest(@Valid RequestEntity requestEntity, Errors errors, @RequestParam("reqid") int reqid,
			@RequestParam("reqcode") String reqcode, @RequestParam("reqtitle") String reqtitle,
			@RequestParam("piority") int piority, @RequestParam("severity") int severity,
			@RequestParam("reqdesc") String reqdesc, @RequestParam("reqdeptcode") String reqtodepart,
			@RequestParam("reqassignto") int reqtoperson, @RequestParam("reqinicomment") String reqfstcomment,
			@RequestParam("sestdesc") String reqstatus, HttpSession session, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else {
			try {
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);
				char stuscod = reqstatus.charAt(0);

				if (errors.hasErrors()) {
					return "redirect:/home/user/dashboard";
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

						session.setAttribute("message",
								new Message("Request Update Successfully !! Your Request Refernece No : " + reqcode,
										"alert-success"));
						return "redirect:user/viewrequest";
					} else {
						session.setAttribute("message",
								new Message("Request Update Fail !! Please try Againg ", "alert-danger"));
						return "redirect:/home/user/dashboard";
					}
				}
			} catch (Exception e) {
				System.out.println(e);
				session.setAttribute("message", new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
				errors.hasErrors();
				return "redirect:/home/user/dashboard";
			}
		}
	}

	/* ===== Save New User PROCESS ===== */
	@RequestMapping("/savenewuserprocess")
	public String savenewuser(@Valid @ModelAttribute("userEntity") UserEntity userEntity, Errors errors,
			HttpSession session, HttpServletResponse response) {
		if ((session.getAttribute("username") == null)) {
			System.out.println(session.getAttribute("username"));
			return "redirect:/home/";
		} else if ((!session.getAttribute("isUserAdmin").equals("admin"))
				&& (!session.getAttribute("isUserAdmin").equals("supadmin"))) {
			System.out.println("You are not a valid User!!");
			session.setAttribute("message", new Message("You are not a valid User!!", "alert-danger"));
			return "index";
		} else {
			try {
				if (errors.hasErrors()) {
					return "user/createuser";
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
						MailResponse emailw = service.sendNewUserEmail(isUserSave.getuEmail(), model);
						session.setAttribute("message",
								new Message("User Create Successfully !! New Username is  :  " + isUserSave.getuName(),
										"alert-success"));
						return "redirect:user/getalluser";
					} else {
						session.setAttribute("message",
								new Message("Invalid Data Or Data Not Save !!", "alert-danger"));
						return "user/createuser";
					}
				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Invalid Data Or Data Not Save  Catch!!", "alert-danger"));
				errors.hasErrors();
				return "user/createuser";
			}
		}
	}

	/* ======== Save list of Department user role ======== */
	@PostMapping(path = "/saveuserrole")
	public String SaveUserDeptRole(@RequestBody List<UserDeptEntity> userDeptEntity, HttpSession session,
			HttpServletResponse response) {
		System.out.println((int) session.getAttribute("userId"));

		int DUDA = this.userDeptService.DeleteUserDeptAccess((int) session.getAttribute("userId"));

		UserDeptEntity entity = null;

		entity = this.userDeptService.saveUserDeptAccess(userDeptEntity);
		System.out.println(DUDA);
		if (DUDA > 0) {

		}

		if (entity != null) {
			session.setAttribute("message",
					new Message("User Role Successfully Save For!! :  " + entity.getUserid(), "alert-success"));
			return "redirect:user/getalluser";
		} else {
			session.setAttribute("message", new Message("User Role in not Save !! :  ", "alert-danger"));
			return "redirect:user/getalluser";
		}

	}

	/* ======== Update user date PROCESS ======== */
	@PostMapping("/updateusersprocess")
	public String updateUser(@Valid @ModelAttribute("userEntity") UserEntity userEntity, Errors errors, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		HttpSession session = request.getSession(false);
		userEntity.setuId((int) session.getAttribute("userId"));
		userEntity.setuCBy((String) session.getAttribute("oldcreby"));
		userEntity.setuCDate((Date) session.getAttribute("oldcredt"));
		if (userEntity.getuPassword() == null || userEntity.getuPassword().isEmpty()) {
			userEntity.setuPassword((String) session.getAttribute("usercurrpass"));
		}
		this.userService.CreateNewUser(userEntity);
		session.removeAttribute((String) session.getAttribute("userId").toString());
		session.removeAttribute((String) session.getAttribute("userFname"));
		session.removeAttribute((String) session.getAttribute("userfullname"));
		session.removeAttribute((String) session.getAttribute("usercurrpass"));
		session.removeAttribute((String) session.getAttribute("oldcreby").toString());
		session.removeAttribute((String) session.getAttribute("oldcredt").toString());
		return "redirect:user/getalluser";
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