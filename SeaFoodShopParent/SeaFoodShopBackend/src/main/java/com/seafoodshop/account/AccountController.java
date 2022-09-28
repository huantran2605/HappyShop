package com.seafoodshop.account;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.seafoodshop.FileUploadUtil;
import com.seafoodshop.common.entity.User;
import com.seafoodshop.security.UserDetailsClass;
import com.seafoodshop.user.UserService;

@Controller
@RequestMapping("account")
public class AccountController {

	@Autowired
	UserService userService;

	@GetMapping("/information")
	public String profile(@AuthenticationPrincipal UserDetailsClass loggedAcc,
			@RequestParam("message") Optional<String> message,Model model) {
		User user = userService.findByEmail(loggedAcc.getUsername());
		model.addAttribute("user", user);
		model.addAttribute("message", message.orElse(null));
		return "account/profile";
	}

	@PostMapping("/updateAccount")
	public String updateAccount(User user, @RequestParam("image") MultipartFile mutipartFile,
			@AuthenticationPrincipal UserDetailsClass loggedAcc,
			RedirectAttributes re, Model model) throws IOException {

		User oldUser = userService.findByEmail(user.getEmail());
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
			user.setPhoto(oldUser.getPhoto());			
		}

		BeanUtils.copyProperties(user, oldUser);

		userService.save(oldUser);

		loggedAcc.setFirstname(user.getFirstName());
		loggedAcc.setLastname(user.getLastName());

		re.addAttribute("message", "Update successfully!");
	
		return "redirect:/account/information";
	}
	
	@GetMapping("/changePasswordForm")
	public String changePasswordForm(@RequestParam("message") Optional<String> message, Model model) {
		model.addAttribute("message", message.orElse(null));
		return "account/password";
	}
	@PostMapping("/changePassword")
	public String changePassword(@AuthenticationPrincipal UserDetailsClass loggedAcc,
			@RequestParam("password") String password,RedirectAttributes re) {
		User user = userService.findByEmail(loggedAcc.getUsername());
		//encode password
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passEncoder = encoder.encode(password);
		
		user.setPassword(passEncoder);		
		userService.save(user);
		re.addAttribute("message", "Change Password Successful!");
		return "redirect:/account/changePasswordForm";
	}
}
