package com.seafoodshop.user.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.seafoodshop.FileUploadUtil;
import com.seafoodshop.common.entity.Role;
import com.seafoodshop.common.entity.User;
import com.seafoodshop.role.RoleService;
import com.seafoodshop.user.UserService;
import com.seafoodshop.user.export.UserCsvExporter;
import com.seafoodshop.user.export.UserExcelExporter;
import com.seafoodshop.user.export.UserPdfExporter;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@GetMapping("/listUser")
	private String viewFirstPageUser(Model model,
			RedirectAttributes re,
			@RequestParam("message")  Optional<String> message) {
		List<User> list = userService.findAll();
		model.addAttribute("users", list);
		
		re.addAttribute("message", message.orElse(null));
		return "redirect:/user/page/1?sortField=id&sortDir=asc&keyWord=";
	}
	
	@GetMapping("/new")
	private String form_User(Model model, User user) {
		List<Role> list = roleService.findAll();
		user.setEnable(true);
		model.addAttribute("user", user);  
		model.addAttribute("roles", list);
		
		return "user/form_create_user";
	}

	@RequestMapping("/saveProcess")
	private String saveUser(User user, @RequestParam("image") MultipartFile mutipartFile, RedirectAttributes re,
			Model model) throws IOException {

		// encode pass
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = user.getPassword();
		String passEncoder = encoder.encode(password);
		user.setPassword(passEncoder);
		// upload photo
		if (!mutipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mutipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User savedUser = userService.save(user);
			String fileDir = "users-photo/" + savedUser.getId();
			// delete old photos if have
			FileUploadUtil.cleanDir(fileDir);
			FileUploadUtil.saveFile(mutipartFile, fileName, fileDir);

		} else {
			user.setPhoto(null);
			userService.save(user);
			re.addAttribute("message", "Added new User successfully!");
		}
		return "redirect:/user/page/1?sortField=id&sortDir=asc&keyWord=" + user.getEmail();
	}
	@RequestMapping("/updateProcess")
	private String saveUser(User user, @RequestParam("image")  MultipartFile mutipartFile,
			RedirectAttributes re,
			@RequestParam("id") Optional<Long> id ,
			Model model) throws IOException {
		
		Optional<User> oldUser = userService.findById(id.get());
		//encode pass
		if(!user.getPassword().isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String passEncoder = encoder.encode(user.getPassword());
			user.setPassword(passEncoder);			
		}
		else {
			user.setPassword(oldUser.get().getPassword());
		}	
		//upload photo
		if(!mutipartFile.isEmpty()) {
			String fileName= StringUtils.cleanPath(mutipartFile.getOriginalFilename());
			user.setPhoto(fileName);
			User savedUser = userService.save(user);
			String fileDir = "users-photo/" + savedUser.getId();
			//delete old photos if have
			FileUploadUtil.cleanDir(fileDir);
			FileUploadUtil.saveFile(mutipartFile, fileName, fileDir);		 
			
		}
		else {
			//if when update no photo set old photo, update option
		
				user.setPhoto(oldUser.get().getPhoto());	
				
				BeanUtils.copyProperties(user, oldUser.get());
				userService.save(oldUser.get());
				re.addAttribute("message", "Updated User successfully!");
		
	}
		return "redirect:/user/page/1?sortField=id&sortDir=asc&keyWord="+ user.getEmail();
	}

	@GetMapping("update/{id}")
	private String updateUser(@PathVariable("id") Long id,
					Model model) {
		Optional<User> user =  userService.findById(id);
		if (user.isEmpty()) {
			model.addAttribute("message", "The user is not exist!");
			return "redirect:/user/listUser";
		}
		else {
			List<Role> list = roleService.findAll();
			model.addAttribute("roles", list);
			model.addAttribute("user", user.get());
			return "user/update";
		}
			
	}
	
	
	@GetMapping("/delete/{id}")
	private String deleteUser(@PathVariable("id") Long id,
			RedirectAttributes re,Model model) throws IOException {
		Optional<User> user =  userService.findById(id);
		if (user.isEmpty()) {
			re.addAttribute("message", "The user is not exist!");
			return "redirect:/user/listUser";
		}
		else {
			userService.deleteById(id);	
			re.addAttribute("message","Delete User has id: "+ id + " successfully!");			
			return "redirect:/user/listUser";
		}
	
	}
	
	@GetMapping("updateEnabled/{id}")
	private String updateEnableStatus(@PathVariable("id") Long id,
			RedirectAttributes re){
		Optional<User> user =  userService.findById(id);
		String status = "";
		if (user.isEmpty()) {
			re.addAttribute("message", "The user is not exist!");
			return "redirect:/user/listUser";
		}
		else {
			status = userService.updateEnabledStatus(user.get());
			re.addAttribute("message", status);		
		}
		return "redirect:/user/listUser";
	}

	@GetMapping("/page/{pageNum}")
	private String userPage (@PathVariable ("pageNum") Integer pageNum,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyWord") Optional<String> keyWord,	
			
			Model model,@RequestParam("message")  Optional<String> message) {
		//sort
		Sort sort = Sort.by(sortField);
		if(sortDir.equalsIgnoreCase("asc"))
			sort = Sort.by(sortField).ascending();
		else  sort = Sort.by(sortField).descending();
		
		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1,
				userService.SIZE_PAGE_USER, sort);
	
		Page<User> pageUser = userService.findAll(pageable, keyWord.get()); 
		List<User> listUser = pageUser.getContent();
		//list user 
				
		long startCount = (pageNum - 1) * userService.SIZE_PAGE_USER + 1;
		long endCount = startCount + userService.SIZE_PAGE_USER - 1 ;
		if(endCount > pageUser.getTotalElements() )
			endCount = pageUser.getTotalElements();
		
		String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
 		model.addAttribute("reserveDir", reserveDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("totalPage", pageUser.getTotalPages()); 
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		if(endCount >  pageUser.getTotalElements()) {
			endCount = pageUser.getTotalElements();
		}
		
		model.addAttribute("totalElement", pageUser.getTotalElements());
		
		model.addAttribute("users", listUser );
		
		model.addAttribute("usersCurrentPerPage", pageUser.getNumberOfElements());
		model.addAttribute("usersPerPage", userService.SIZE_PAGE_USER);
		model.addAttribute("message", message.orElse(null));
		
		model.addAttribute("keyWord", keyWord.get());

		return"user/listUser";
	}
	@GetMapping("/export/csv")
	public void exportCsv(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.findAll();
		UserCsvExporter userCsvExporter = new UserCsvExporter();
		userCsvExporter.export(listUser, response);

	}
	@GetMapping("/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.findAll();
		UserExcelExporter userExcelExporter = new UserExcelExporter();
		userExcelExporter.export(listUser, response);
		
	}
	@GetMapping("/export/pdf")
	public void exporPdf(HttpServletResponse response) throws IOException {
		List<User> listUser = userService.findAll();
		UserPdfExporter userPdfExporter = new UserPdfExporter();
		userPdfExporter.export(listUser, response);
		
	}
	
}
