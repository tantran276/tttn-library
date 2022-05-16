package com.example.tttn.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.Category;
import com.example.tttn.entity.ERole;
import com.example.tttn.entity.Role;
import com.example.tttn.repository.CategoryRepository;
import com.example.tttn.repository.RoleRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/init")
public class InitController {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@PostMapping()
	public ResponseEntity<?> initData() {
		roleRepository.save(new Role(ERole.USER));
		roleRepository.save(new Role(ERole.ADMIN));
		categoryRepository.save((new Category("Tin học, thông tin & tác phẩm tổng quát")));
		categoryRepository.save((new Category("Triết học, cận tâm lý và thuyết huyền bí và tâm lý học")));
		categoryRepository.save((new Category("Tôn giáo")));
		categoryRepository.save((new Category("Khoa học xã hội")));
		categoryRepository.save((new Category("Ngôn ngữ")));
		categoryRepository.save((new Category("Khoa học tự nhiên và toán học")));
		return ResponseEntity.ok("Success");
	}
}
