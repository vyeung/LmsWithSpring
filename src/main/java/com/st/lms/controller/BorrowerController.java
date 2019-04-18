package com.st.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.st.lms.service.BorrowerService;
import com.st.lms.models.*;
import com.st.lms.dto.*;
import com.st.lms.exception.*;

@RestController
@RequestMapping("/borrower")
public class BorrowerController {

	@Autowired
	BorrowerService borrowerService;
	
	//return list of branches where the borrower has borrowed books from
	@GetMapping("/cardNo/{cardNo}/libraries")
	public List<BkLoansBranchDTO> getAllBranches(@PathVariable int cardNo) throws NotFoundException{
		if(!borrowerService.cardNoExists(cardNo)) {
			throw new NotFoundException("Login", "borrower", cardNo);
		}
		return borrowerService.getBranchesWithBkLoans(cardNo);
	}
	
	@GetMapping("/cardNo/{cardNo}/libraries/{branchId}/books")
	public List<BkLoansBkAuthDTO> getAllLoans(@PathVariable int cardNo, @PathVariable int branchId) throws NotFoundException{
		if(!borrowerService.cardNoExists(cardNo)) {
			throw new NotFoundException("Login", "borrower", cardNo);
		}
		else if(!borrowerService.branchExists(branchId)) {
			throw new NotFoundException("Search", "library branch", branchId);
		}
		return borrowerService.getBooksFromBranch(cardNo, branchId);
	}
	
	//borrow a book
	@PutMapping("/cardNo/{cardNo}/libraries/{branchId}/books/{bookId}")
	public ResponseEntity<BookLoans> borrowBook(@PathVariable int cardNo, @PathVariable int branchId, @PathVariable int bookId) throws NotFoundException, BadRequestException{
		if(!borrowerService.cardNoExists(cardNo)) {
			throw new NotFoundException("Login", "borrower", cardNo);
		}
		else if(!borrowerService.branchExists(branchId)) {
			throw new NotFoundException("Check out", "library branch", branchId);
		}
		else if(!borrowerService.loanExists(cardNo, branchId, bookId)) {
			throw new NotFoundException("Check out", "book loan", bookId);
		}
		int noOfCopies = borrowerService.getNoOfCopies(bookId, branchId);
		if(noOfCopies < 1) {
			throw new BadRequestException("Check out failed. That library does not have any copies available.");
		}
		else {
			borrowerService.checkOutBook(bookId, branchId, cardNo, noOfCopies);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	//return a book
	@DeleteMapping("/cardNo/{cardNo}/libraries/{branchId}/books/{bookId}")
	public ResponseEntity<BookLoans> returnBook(@PathVariable int cardNo, @PathVariable int branchId, @PathVariable int bookId) throws NotFoundException, BadRequestException{
		if(!borrowerService.cardNoExists(cardNo)) {
			throw new NotFoundException("Login", "borrower", cardNo);
		}
		else if(!borrowerService.branchExists(branchId)) {
			throw new NotFoundException("Return", "library branch", branchId);
		}
		else if(!borrowerService.loanExists(cardNo, branchId, bookId)) {
			throw new NotFoundException("Return", "book loan", bookId);
		}
		if(!borrowerService.loanExists(cardNo, branchId, bookId)) {
			throw new BadRequestException("Return failed. You do not have that book checked out!");
		}
		else {
			int noOfCopies = borrowerService.getNoOfCopies(bookId, branchId);
			borrowerService.returnBook(bookId, branchId, cardNo, noOfCopies);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}