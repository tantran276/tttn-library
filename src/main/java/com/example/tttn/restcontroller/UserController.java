package com.example.tttn.restcontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.Role;
import com.example.tttn.entity.User;
import com.example.tttn.mapper.ProfileMapper;
import com.example.tttn.mapper.UserInfoMapper;
import com.example.tttn.payload.request.UserInfo;
import com.example.tttn.payload.response.Profile;
import com.example.tttn.secutiry.jwt.JwtUtils;
import com.example.tttn.service.RoleService;
import com.example.tttn.service.interfaces.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RoleService roleService;
	
	@GetMapping("/me")
//	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')" )
	public ResponseEntity<?> getUser(@RequestHeader HttpHeaders header) {
		String authorization = header.getFirst("Authorization");
		String username = jwtUtils.getUserNameFromJwtToken(authorization.substring(7));
		Profile profile = ProfileMapper.toProfile(userService.getUserByUsername(username));
		return ResponseEntity.ok(profile);
	}
	
	@GetMapping()
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getUsers() {
		List<Profile> profiles = new ArrayList<>();
		userService.getAllUsers().forEach((user) -> {
			profiles.add(ProfileMapper.toProfile(user));
		});
		return ResponseEntity.ok(profiles);
	}
	
	@PutMapping()
//	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')" )
	public ResponseEntity<?> updateUser(@RequestBody UserInfo userInfo) {
		User user = userService.getUserById(userInfo.getId());
		user = UserInfoMapper.toUser(user, userInfo);
		user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		Set<Role> roles = new HashSet<>();
		if(userInfo.getRole()!= null && !userInfo.getRole().equals("")) {
			roles.add(roleService.getByName(userInfo.getRole()));
		}
		if(userInfo.getRole1()!= null && !userInfo.getRole1().equals("")) {
			roles.add(roleService.getByName(userInfo.getRole1()));
		}
		user.setRoles(roles);
		userService.saveUser(user);
		return ResponseEntity.ok("Đã cập thành công");
	}
	
	@DeleteMapping("/{id}")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> deleteUsers(@PathVariable long id) {
		userService.deleteById(id);
		return ResponseEntity.ok("Đã xóa user: " + id);
	}
}
