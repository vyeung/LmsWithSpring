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
			throw new NotFoundException("Get", "library branch", branchId);
		return new ResponseEntity<LibraryBranch>(branch, HttpStatus.OK);
	}
	
	@PutMapping("/libraries/{branchId}")
	public ResponseEntity<LibraryBranch> updateLibraryBranch(@PathVariable int branchId, @RequestBody LibraryBranch branch) throws NotFoundException{
		if( librarianService.getLibraryBranch(branchId) == null)
			throw new NotFoundException("Update", "library branch", branchId);
		librarianService.updateBranch(branchId, branch.getBranchName(), branch.getBranchAddress());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/libraries/{id}/book_copies")
	public ResponseEntity<List<BkCopiesDTO>> getBookCopies(@PathVariable int id) throws NotFoundException {
		List<BkCopiesDTO> list = librarianService.getBookCopiesBookAndTitle(id);
		if( list.size() == 0) {
			throw new NotFoundException("Get BookCopies failed. The library branch with id=" + id + " not found");
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PutMapping("/libraries/{branchId}/book_copies/{bookId}")
	public ResponseEntity<BkCopiesDTO> updateBookCopies(@PathVariable int branchId,@PathVariable int bookId, @RequestBody BookCopies bkCopy) throws NotFoundException{
		List<BkCopiesDTO> list = librarianService.getBookCopiesBookAndTitle(branchId);
		if( list.size() == 0) {
			throw new NotFoundException("Update BookCopies failed. Branch with id=" + branchId + " not found");
		}
		
		Book book = librarianService.getBook(bookId);
		if( book == null) {
			throw new NotFoundException("Update BookCopies failed. Book with id=" + bookId + " not found");
		}
		
		librarianService.updateNumCopies(bookId, branchId, bkCopy.getNoOfCopies());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}