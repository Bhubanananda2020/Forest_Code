package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add_contact")
	public String openAddContactFrom(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			if (file.isEmpty()) {
				contact.setImage("/image/contact.png");
				System.out.println("File Is Empty");
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is Uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("Contact add to Database");
			session.setAttribute("message", new Message("Your Contact is add !! ", "success"));

		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong !! try again ", "danger"));
			System.out.println("Error" + e.getMessage());
			e.printStackTrace();
		}
		return "normal/add_contact_form";
	}

	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal, HttpSession session) {
		m.addAttribute("title", "Shows Contact");
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		Pageable pageable = PageRequest.of(page, 4);

		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	@GetMapping("{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Principal principal, Model model) {
		String name = principal.getName();
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		User user = this.userRepository.getUserByUserName(name);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("title", contact.getName());
			model.addAttribute("contact", contact);
		}
		return "normal/contact_detail";
	}

	@GetMapping("/delete/{cid}")
	public String deletecontact(@PathVariable("cid") Integer cid, Principal principal, Model model,
			HttpSession session) {
		// String name = principal.getName();

		Contact contact = this.contactRepository.findById(cid).get();

		contact.setUser(null);

		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);

		this.userRepository.save(user);

		session.setAttribute("message", new Message("Contact Deleted Successfully", "success"));
		return "redirect:/user/show_contacts/0	";
	}

	@PostMapping("/update_contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m) {
		m.addAttribute("title", "Update Contact'");
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("contact", contact);
		return "normal/update_form";
	}

	@RequestMapping(value = "/process_update", method = RequestMethod.POST)
	public String updateFormHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {

			Contact oldContactDetails = this.contactRepository.findById(contact.getCid()).get();
			if (!file.isEmpty()) {

				// Delete Photo
				File deleteFile = new ClassPathResource("static/image").getFile();
				File File1 = new File(deleteFile, oldContactDetails.getImage());
				File1.delete();

				// Upload photo
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				System.out.println("Image is Uploaded");

			} else {
				contact.setImage(oldContactDetails.getImage());
			}

			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is updated ", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + contact.getCid() + "/contact";
	}

	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}
	
	
	

}