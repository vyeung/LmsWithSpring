package com.st.lms.controller;

import java.sql.Date;
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

import com.st.lms.dto.BkAuthPubDTO;
import com.st.lms.exception.AlreadyExistsException;
import com.st.lms.exception.BadRequestException;
import com.st.lms.exception.NotFoundException;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookLoans;
import com.st.lms.models.Borrower;
import com.st.lms.models.LibraryBranch;
import com.st.lms.models.Publisher;
import com.st.lms.service.AdminService;
import com.st.lms.utils.DateCalculations;

@RestController
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/publishers")
	public List<Publisher> getAllPublishers() {
		return adminService.getAllPublishers();
	}
	
	@GetMapping("/publisher/{id}")
	public ResponseEntity<Publisher> getPublisher(@PathVariable int id) throws NotFoundException {
		Publisher publisher = adminService.getPublisher(id);
		if(publisher == null) {
			throw new NotFoundException("Publisher with id=" + id + " not found");
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	}
	
	@PostMapping("/publisher")
	public ResponseEntity<Publisher> addPublisher(@RequestBody Publisher pub) throws AlreadyExistsException {
		List<Publisher> pubs = adminService.getAllPublishers();
		for(Publisher p : pubs) {
			if(p.getPublisherName().equals(pub.getPublisherName())) {
				throw new AlreadyExistsException("Publisher " + pub.getPublisherName() + " already exists in the database");
			}
		}
		
		//ok if code gets here
		adminService.addPublisher(pub);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/publisher/{id}")
	public ResponseEntity<Publisher> updatePublisher(@PathVariable int id, @RequestBody Publisher pub) 
	throws NotFoundException {
		
		Publisher publisher = adminService.getPublisher(id);
		if(publisher == null) {
			throw new NotFoundException("Update failed. Publisher with id=" + id + " not found");
		}
		
		adminService.updatePublisher(id, pub);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/publisher/{id}")
	public ResponseEntity<Publisher> deletePublisher(@PathVariable int id) throws NotFoundException {
		Publisher publisher = adminService.getPublisher(id);
		if(publisher == null) {
			throw new NotFoundException("Delete failed. Publisher with id=" + id + " not found");
		}
		
		adminService.deletePublisher(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*###################################################################################*/
	
	@GetMapping("/books")
	public List<BkAuthPubDTO> getAllBooks() {
		return adminService.getBooksWithAuthAndPub();
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<BkAuthPubDTO> getBook(@PathVariable int id) throws NotFoundException {
		BkAuthPubDTO bookWithAuthPub = adminService.getBookWithAuthAndPub(id);
		if(bookWithAuthPub == null) {
			throw new NotFoundException("Book with id=" + id + " not found");
		}
		
		return new ResponseEntity<>(bookWithAuthPub, HttpStatus.OK);
	}
	
	@PostMapping("/book")
	public ResponseEntity<BkAuthPubDTO> addBook(@RequestBody BkAuthPubDTO bookBody) 
	throws AlreadyExistsException, BadRequestException {
		
		//check if title, author, and publisher name exists in DB
		boolean titleExists = adminService.bookTitleExists(bookBody.getBookTitle());
		boolean authorExists = adminService.authNameExists(bookBody.getAuthorName());
		boolean publisherExists = adminService.pubNameExists(bookBody.getPublisherName());
		
		//throw exceptions depending on flag values
		if(titleExists && authorExists) {
			throw new AlreadyExistsException(bookBody.getBookTitle() + " by " + bookBody.getAuthorName() + " already exists in the database");
		}
		else if(!titleExists && !authorExists) {
			throw new BadRequestException("Add author " + bookBody.getAuthorName() + " to the database first");
		}
		else if(titleExists && !authorExists) {
			throw new BadRequestException("Add author " + bookBody.getAuthorName() + " to the database first");
		}
		else if(!titleExists && !publisherExists) {
			throw new BadRequestException("Add publisher " + bookBody.getPublisherName() + " to the database first");
		}
		else if(titleExists && !publisherExists) {
			throw new BadRequestException("Add publisher " + bookBody.getPublisherName() + " to the database first");
		}
		
		//no exceptions if code gets here.
		//titleExists=False, authorExists=True, publisherExists=True is only safe combination to add a book.
		//find corresponding author and publisher ids.
		int authId = 0, pubId = 0;
		List<Author> authors = adminService.getAllAuthors();
		for(Author a : authors) {
			if(bookBody.getAuthorName().equals(a.getAuthorName()))
				authId = a.getAuthorId();
		}
		List<Publisher> pubs = adminService.getAllPublishers();
		for(Publisher p : pubs) {
			if(bookBody.getPublisherName().equals(p.getPublisherName()))
				pubId = p.getPublisherId();
		}
		
		adminService.addBook(bookBody.getBookTitle(), authId, pubId);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/book/{id}")
	public ResponseEntity<BkAuthPubDTO> updateBook(@PathVariable int id, @RequestBody BkAuthPubDTO bookBody) 
	throws NotFoundException, BadRequestException {
		
		Book myBook = adminService.getBook(id);
		if(myBook == null) {
			throw new NotFoundException("Update failed. Book with id=" + id + " not found");
		}
		
		boolean authorExists = adminService.authNameExists(bookBody.getAuthorName());
		boolean publisherExists = adminService.pubNameExists(bookBody.getPublisherName());
		
		//throw exceptions depending on flag values
		if(!authorExists) {
			throw new BadRequestException("Add author " + bookBody.getAuthorName() + " to the database first");
		}
		else if(!publisherExists) {
			throw new BadRequestException("Add publisher " + bookBody.getPublisherName() + " to the database first");
		}
		
		//find corresponding author and publisher ids
		int authId = 0, pubId = 0;
		List<Author> authors = adminService.getAllAuthors();
		for(Author a : authors) {
			if(bookBody.getAuthorName().equals(a.getAuthorName()))
				authId = a.getAuthorId();
		}
		List<Publisher> pubs = adminService.getAllPublishers();
		for(Publisher p : pubs) {
			if(bookBody.getPublisherName().equals(p.getPublisherName()))
				pubId = p.getPublisherId();
		}
		
		Book updates = new Book();
		updates.setTitle(bookBody.getBookTitle());
		updates.setAuthorId(authId);
		updates.setPubId(pubId);
		adminService.updateBook(id, updates);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<BkAuthPubDTO> deleteBook(@PathVariable int id) throws NotFoundException {
		Book b = adminService.getBook(id);
		if(b == null) {
			throw new NotFoundException("Delete failed. Book with id=" + id + " not found");
		}
		
		adminService.deleteBook(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*###################################################################################*/
	
	@GetMapping("/bookLoans")
	public List<BookLoans> getAllBookLoans() {
		return adminService.getAllBookLoans();
	}
	
	@PutMapping("/bookLoans/bookId/{bookId}/branchId/{branchId}/cardNo/{cardNo}")
	public ResponseEntity<BookLoans> updateBookLoanDueDate(@PathVariable int bookId, @PathVariable int branchId, @PathVariable int cardNo, @RequestBody BookLoans newDueDate) 
	throws NotFoundException {

		List<BookLoans> bookLoans = adminService.getAllBookLoans();
		Date dateOut = null;
		int found = 0;

		//check if all 3 ids are a row in BookLoans
		for(BookLoans bl : bookLoans) {
			if(bl.getBookId()==bookId && bl.getBranchId()==branchId && bl.getCardNo()==cardNo) {
				found = 1;
				dateOut = bl.getDateOut();
				break;
			}
		}
		
		if(found == 0) 
			throw new NotFoundException("BookLoan with bookId=" + bookId + ", branchId=" + branchId + ", cardNo=" + cardNo + " not found");

		//ok
		Date dueDate = (Date) DateCalculations.addOneDay(newDueDate.getDueDate());  //fixes off by one anomaly
		adminService.changeDueDate(bookId, branchId, cardNo, dateOut, dueDate);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*###################################################################################*/
	
	@GetMapping("/authors")
	public List<Author> getAllAuthors() {
		return adminService.getAllAuthors();
	}
	
	@GetMapping("/author/{id}")
	public ResponseEntity<Author> getAuthor(@PathVariable int id) throws NotFoundException {
		Author author = adminService.getAuthor(id);
		if(author == null) {
			throw new NotFoundException("Author with id=" + id + " not found");
		}
		
		return new ResponseEntity<>(author, HttpStatus.OK);
	}
	
	@PostMapping("/author")
	public ResponseEntity<Author> addAuthor(@RequestBody Author author) throws AlreadyExistsException {
		List<Author> authors = adminService.getAllAuthors();
		for(Author a : authors) {
			if(a.getAuthorName().equals(author.getAuthorName())) {
				throw new AlreadyExistsException("Author " + author.getAuthorName() + " already exists in the database");
			}
		}
		
		adminService.addAuthor(author);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/author/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody Author author) 
	throws NotFoundException {
		
		Author existingAuthor = adminService.getAuthor(id);
		if(existingAuthor == null) {
			throw new NotFoundException("Update failed. Author with id=" + id + " not found");
		}
		
		adminService.updateAuthor(id, author);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/author/{id}")
	public ResponseEntity<Author> deleteAuthor(@PathVariable int id) throws NotFoundException {
		Author author = adminService.getAuthor(id);
		if(author == null) {
			throw new NotFoundException("Delete failed. Author with id=" + id + " not found");
		}
		
		adminService.deleteAuthor(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/borrowers")
	public List<Borrower> getAllBorrowers() {
		return adminService.getAllBorrowers();
	}
	
	@GetMapping("/borrower/{id}")
	public ResponseEntity<Borrower> getBorrower(@PathVariable int id) throws NotFoundException {
		Borrower borrower = adminService.getBorrower(id);
		if(borrower == null) {
			throw new NotFoundException("Borrower with id=" + id + " not found");
		}
		
		return new ResponseEntity<>(borrower, HttpStatus.OK);
	}
	
	@PostMapping("/borrower")
	public ResponseEntity<Borrower> addBorrower(@RequestBody Borrower borr) throws AlreadyExistsException {
		List<Borrower> borrowers = adminService.getAllBorrowers();
		for(Borrower b : borrowers) {
			if(b.getName().equals(borr.getName())) {
				throw new AlreadyExistsException("Borrower " + borr.getName() + " already exists in the database");
			}
		}
		
		adminService.addBorrower(borr);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/borrower/{id}")
	public ResponseEntity<Borrower> updateBorrower(@PathVariable int id, @RequestBody Borrower borr) 
	throws NotFoundException {
		
		Borrower borrower = adminService.getBorrower(id);
		if(borrower == null) {
			throw new NotFoundException("Update failed. Borrower with id=" + id + " not found");
		}
		
		adminService.updateBorrower(id, borr);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/borrower/{id}")
	public ResponseEntity<Borrower> deleteBorrower(@PathVariable int id) throws NotFoundException {
		Borrower borrower = adminService.getBorrower(id);
		if(borrower == null) {
			throw new NotFoundException("Delete failed. Borrower with id=" + id + " not found");
		}
		
		adminService.deleteBorrower(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/libraryBranches")
	public List<LibraryBranch> getAllLibraryBranches() {
		return adminService.getAllBranches();
	}
	
	@GetMapping("/libraryBranch/{id}")
	public ResponseEntity<LibraryBranch> getLibraryBranch(@PathVariable int id) throws NotFoundException {
		LibraryBranch libraryBranch = adminService.getLibraryBranch(id);
		if(libraryBranch == null) {
			throw new NotFoundException("LibraryBranch with id=" + id + " not found");
		}
		
		return new ResponseEntity<>(libraryBranch, HttpStatus.OK);
	}
	
	@PostMapping("/libraryBranch")
	public ResponseEntity<LibraryBranch> addLibraryBranch(@RequestBody LibraryBranch lb) throws AlreadyExistsException {
		List<LibraryBranch> branches = adminService.getAllBranches();
		for(LibraryBranch b : branches) {
			if(b.getBranchName().equals(lb.getBranchName())) {
				throw new AlreadyExistsException("LibraryBranch " + lb.getBranchName() + " already exists in the database");
			}
		}
		
		//ok if code gets here
		adminService.addLibraryBranch(lb);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/libraryBranch/{id}")
	public ResponseEntity<LibraryBranch> updateLibraryBranch(@PathVariable int id, @RequestBody LibraryBranch lb) 
	throws NotFoundException {
		
		LibraryBranch libraryBranch = adminService.getLibraryBranch(id);
		if(libraryBranch == null) {
			throw new NotFoundException("Update failed. LibraryBranch with id=" + id + " not found");
		}
		
		adminService.updateLibraryBranch(id, lb);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/libraryBranch/{id}")
	public ResponseEntity<LibraryBranch> deleteLibraryBranch(@PathVariable int id) throws NotFoundException {
		LibraryBranch libraryBranch = adminService.getLibraryBranch(id);
		if(libraryBranch == null) {
			throw new NotFoundException("Delete failed. LibraryBranch with id=" + id + " not found");
		}
		
		adminService.deleteLibraryBranch(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}