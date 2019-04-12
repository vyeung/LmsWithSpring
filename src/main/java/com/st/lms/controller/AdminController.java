package com.st.lms.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.st.lms.models.*;
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
	
	//TODO: Author
	@GetMapping(path="/author/{authorId}", produces= {"application/json"})
    public ResponseEntity<com.st.lms.models.Author> getAuthor(@PathVariable("authorId") int authorId){
		com.st.lms.models.Author author;
        ResponseEntity<com.st.lms.models.Author> response;
        
        author = adminService.getAuthor(authorId);
        if(author == null)
            response = new ResponseEntity<Author>(author, HttpStatus.BAD_REQUEST);
        else {
            response = new ResponseEntity<Author>(author, HttpStatus.OK);
        }
        return response;
		
	}
	
	@PostMapping(path="/author/")
	public ResponseEntity<Author> postAuthor(@RequestBody Author author){
        ResponseEntity<Author> response;
        if(adminService.getAuthor(author.getAuthorId()) != null)
            return new ResponseEntity<Author>(author, HttpStatus.CONFLICT);
        return new ResponseEntity<Author>(author, HttpStatus.CREATED);
	}
	
	//TODO: check if {authorId} works if I don't have it as a path variable
	@PutMapping(path="/author/{authorId}")
	public ResponseEntity<Author> Author(@PathVariable("authorId") int authorId){
		Author author;
        ResponseEntity<Author> response;
        author = adminService.getAuthor(authorId);
        
        if(author == null)
            response = new ResponseEntity<Author>(author, HttpStatus.BAD_REQUEST);
        else {
            response = new ResponseEntity<Author>(author, HttpStatus.OK);
        }
        return response;
	}
	
	@DeleteMapping(path="/author/{authorId}")
	public ResponseEntity<Author> deleteAuthor(@PathVariable("authorId") int authorId){
		Author author;
        ResponseEntity<Author> response;
        
        author = adminService.getAuthor(authorId);
        if(author == null)
            response = new ResponseEntity<Author>(author, HttpStatus.BAD_REQUEST);
        else {
            response = new ResponseEntity<Author>(author, HttpStatus.OK);
        }
        return response;
		
	}
}