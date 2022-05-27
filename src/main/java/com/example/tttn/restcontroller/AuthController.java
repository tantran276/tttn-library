package com.example.tttn.restcontroller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.ERole;
import com.example.tttn.entity.Role;
import com.example.tttn.entity.User;
import com.example.tttn.mapper.ProfileMapper;
import com.example.tttn.mapper.SignupRequestMapper;
import com.example.tttn.payload.request.ForgotPasswordRequest;
import com.example.tttn.payload.request.LoginRequest;
import com.example.tttn.payload.request.ResetPasswordRequest;
import com.example.tttn.payload.request.SignupRequest;
import com.example.tttn.payload.response.JwtResponse;
import com.example.tttn.payload.response.MessageResponse;
import com.example.tttn.repository.RoleRepository;
import com.example.tttn.repository.UserRepository;
import com.example.tttn.secutiry.jwt.JwtUtils;
import com.example.tttn.secutiry.service.UserDetailsImpl;
import com.example.tttn.service.interfaces.UserService;

import net.bytebuddy.utility.RandomString;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserService userService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		if(!userService.existByUsername(loginRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username hoặc password không đúng"));
		}
		User user = userService.getUserByUsername(loginRequest.getUsername());
		if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Username hoặc password không đúng"));
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		if (user.isEnabled()) {
			return ResponseEntity.ok(new JwtResponse(jwt, roles, user.getId()));
		} else {
			return ResponseEntity.badRequest().body("Plese verify email!");
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest)
			throws UnsupportedEncodingException, MessagingException {
		System.out.println(signUpRequest.getUsername());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		if (signUpRequest.getDateOfBirth().after(new Date(System.currentTimeMillis()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Ngày sinh phải bé hơn hiện tại"));
		}
		User user = SignupRequestMapper.toUser(signUpRequest);
		System.out.println(user.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByName(ERole.USER).get());
		user.setRoles(roles);
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		userRepository.save(user);
		sendVerifycationEmail(user, signUpRequest.getSiteUrl());
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyCode(@RequestParam("code") String code) {
		User user = userRepository.findByVerificationCode(code);
		if (user == null) {
			return ResponseEntity.badRequest().body("False");
		} else {
			user.setEnabled(true);
			userRepository.save(user);
			return ResponseEntity.ok(ProfileMapper.toProfile(user));
		}
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<?> sendMailResetPassword(@RequestBody ForgotPasswordRequest forgotPasswordResquest)
			throws UnsupportedEncodingException, MessagingException {
		User user = userService.getUserByUsername(forgotPasswordResquest.getUsername());
		if (user == null) {
			return ResponseEntity.badRequest().body("Username not found");
		}
		sendResetPasswordEmail(user, forgotPasswordResquest.getSiteUrl());
		return ResponseEntity.ok("Success");
	}

	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPassowrdRequest) {
		User user = userRepository.findByVerificationCode(resetPassowrdRequest.getCode());
		if (user == null) {
			return ResponseEntity.badRequest().body("False");
		} else {
			user.setPassword(passwordEncoder.encode(resetPassowrdRequest.getNewPassword()));
			userRepository.save(user);
			return ResponseEntity.ok("Success");
		}
	}

	private void sendResetPasswordEmail(User user, String siteUrl)
			throws UnsupportedEncodingException, MessagingException {
		String subject = "Reset Password";
		String senderName = "Library Management";
		String mailContent = "<p> Dear " + user.getFirstName() + ",</p>";
		mailContent += "<p>Please click the link  below to reset your password:</p>";
		String verifyURL =  siteUrl + "?code=" + user.getVerificationCode();
		mailContent += "<a href=\"" + verifyURL + "\">VERIFY</a>";
		mailContent += "<p>Thank you <br>Library Management Team</p>";
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("ttan5383@gmail.com", senderName);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.setText(mailContent, true);
		javaMailSender.send(message);
	}

	private void sendVerifycationEmail(User user, String siteUrl)
			throws UnsupportedEncodingException, MessagingException {
		String subject = "Please verify your registration";
		String senderName = "Library Management";
		String mailContent = "<p> Dear " + user.getFirstName() + ",</p>";
		mailContent += "<p>Please click the link  below to verify to your  registration:</p>";
		String verifyURL = siteUrl + "?code=" + user.getVerificationCode();
		mailContent += "<a href=\"" + verifyURL + "\">VERIFY</a>";
		mailContent += "<p>Thank you <br>Library Management Team</p>";
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("ttan5383@gmail.com", senderName);
		helper.setTo(user.getEmail());
		helper.setSubject(subject);
		helper.setText(mailContent, true);
		javaMailSender.send(message);
	}

}
