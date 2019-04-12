package com.st.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.st.lms.models.Publisher;
import com.st.lms.service.AdminService;

@RestController
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/publishers")
	public List<Publisher> getAllPublishers() {
		return adminService.getAllPublishers();
	}
	
	@GetMapping("/publisher/{id}")
	public Publisher getPublisher(@PathVariable int id) {
		return adminService.getPublisher(id);
	}
	
	@PostMapping("/publisher")
	public void addPublisher(@RequestBody Publisher pub) {
		adminService.addPublisher(pub);
	}
	
	@PutMapping("/publisher/{id}")
	public void updatePublisher(@PathVariable int id, @RequestBody Publisher pub) {
		adminService.updatePublisher(id, pub);
	}
	
	@DeleteMapping("/publisher/{id}")
	public void deletePublisher(@PathVariable int id) {
		adminService.deletePublisher(id);
	}
	
	
}