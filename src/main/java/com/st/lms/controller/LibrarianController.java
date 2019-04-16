package com.st.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.st.lms.dto.BkCopiesDTO;
import com.st.lms.exception.NotFoundException;
import com.st.lms.models.*;
import com.st.lms.service.*;


@RestController
public class LibrarianController {

	@Autowired
	LibrarianService librarianService;
	
	@GetMapping("/libraries")
	public List<LibraryBranch> getAllBranches() {
		return librarianService.getAllBranches();
	}
	
	@GetMapping("/libraries/{branchId}")
	public ResponseEntity<LibraryBranch> getLibraryBranch(@PathVariable int branchId) throws NotFoundException{
		LibraryBranch branch = librarianService.getLibraryBranch(branchId);
		if( branch == null)
			throw new NotFoundException("Get", "libray branch", branchId);
		return new ResponseEntity<LibraryBranch>(branch, HttpStatus.OK);
	}
	
	@PutMapping("/libraries/{branchId}")
	public ResponseEntity<LibraryBranch> updateLibraryBranch(@PathVariable int branchId, @RequestBody LibraryBranch branch) throws NotFoundException{
		if( librarianService.getLibraryBranch(branchId) == null)
			throw new NotFoundException("Update", "libray branch", branchId);
		librarianService.updateBranch(branchId, branch.getBranchName(), branch.getBranchAddress());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/libraries/{id}/book_copies")
	public List<BkCopiesDTO> getBookCopies(@PathVariable int id){
		return librarianService.getBookCopiesBookAndTitle(id);
	}
	
	@PutMapping("/libraries/{branchId}/book_copies/{bookId}/{numCopies}")
	public ResponseEntity<BkCopiesDTO> updateBookCopies(@PathVariable int branchId,@PathVariable int bookId,@PathVariable int numCopies) throws NotFoundException{
		librarianService.updateNumCopies(bookId, branchId, numCopies);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}