package com.st.lms.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.lms.dao.AuthorDao;
import com.st.lms.dao.BookDao;
import com.st.lms.dao.BookLoansDao;
import com.st.lms.dao.BorrowerDao;
import com.st.lms.dao.LibBranchDao;
import com.st.lms.dao.PublisherDao;
import com.st.lms.dto.BkAuthPubDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookLoans;
import com.st.lms.models.Borrower;
import com.st.lms.models.LibraryBranch;
import com.st.lms.models.Publisher;

@Service
public class AdminService {
	
	@Autowired
	private BookDao bookDao;
	@Autowired
	private PublisherDao publisherDao;
	@Autowired
	private BookLoansDao bookLoansDao;
	@Autowired
	private AuthorDao authorDao;
	@Autowired
	private BorrowerDao borrowerDao;
	@Autowired
	private LibBranchDao libraryBranchDao;

	public boolean bookTitleExists(String bookTitle) {
		List<Book> books = bookDao.findAll();
		
		for(Book b : books) {
			if(b.getTitle().equals(bookTitle))
				return true;
		}
		return false;
	}
	
	public void addBook(String title, int authId, int pubId) {
		Book b = new Book();
		b.setTitle(title);
		b.setAuthorId(authId);
		b.setPubId(pubId);

		bookDao.save(b);
		System.out.println("Add Book Success!");
	}
	
	public Book getBook(int bookId) {
		//Book book = null;
		//book = bookDao.findById(bookId).get();
		//return book;
		
		//prevents having to throw NoSuchElementException
		Optional<Book> book;
		book = bookDao.findById(bookId);
		return book.isPresent() ? book.get() : null;
	}
	
	public List<Book> getAllBooks() {
		List<Book> books = null;
		books = bookDao.findAll();
		return books;
	}
	
	public BkAuthPubDTO getBookWithAuthAndPub(int bookId) {
		BkAuthPubDTO obj = null;
		List<Book> books = bookDao.findAll();
		Author author;
		Publisher publisher;
		
		for(Book b : books) {
			if(b.getBookId() == bookId) {
				author = authorDao.findById(b.getAuthorId()).get();
				publisher = publisherDao.findById(b.getPubId()).get();

				obj = new BkAuthPubDTO(bookId, b.getTitle(), 
						author.getAuthorName(), publisher.getPublisherName());
			}
		}
		return obj;
	}
	
	public List<BkAuthPubDTO> getBooksWithAuthAndPub() {
		List<BkAuthPubDTO> list = null;
		
		List<Book> books = bookDao.findAll();
		Author author;
		Publisher publisher;
		
		BkAuthPubDTO obj;
		list = new ArrayList<>();
		
		for(Book b : books) {
			author = authorDao.findById(b.getAuthorId()).get();
			publisher = publisherDao.findById(b.getPubId()).get();
			
			obj = new BkAuthPubDTO(b.getBookId(), b.getTitle(), 
					author.getAuthorName(), publisher.getPublisherName());
			list.add(obj);
		}
		return list;
	}
	
	public void updateBook(int bookId, Book b) {
		Book book = new Book();
		book.setBookId(bookId);
		book.setTitle(b.getTitle());
		book.setAuthorId(b.getAuthorId());
		book.setPubId(b.getPubId());
		
		bookDao.saveAndFlush(book);
		System.out.println("Update Book Success!");
	}
	
	public void deleteBook(int bookId) {
		Book b = new Book();
		b.setBookId(bookId);
		
		bookDao.deleteById(bookId);
		System.out.println("Delete Book Success!");
	}
	
	/*#########################################################################*/
	
	public boolean authNameExists(String authName) {
		List<Author> authors = authorDao.findAll();
		
		for(Author a : authors) {
			if(a.getAuthorName().equals(authName))
				return true;
		}
		return false;
	}
	
	public List<Author> getAllAuthors() {
		return authorDao.findAll();
	}
	
	public Author getAuthor(int id) {	
		Optional<Author> author = authorDao.findById(id);
		if(author.isPresent())
			return author.get();
		return null;
	}
	
	public Author addAuthor(Author author) {
		Optional<Author> existingAuthor = authorDao.findById(author.getAuthorId());
		if(!existingAuthor.isPresent()) 
			return authorDao.save(author);
		return null;
	}
	
	public Author updateAuthor(int id, Author author) {	
		Optional<Author> existingAuthor = authorDao.findById(id);
		if(!existingAuthor.isPresent())
			return null;
		author.setAuthorId(id);
		return authorDao.save(author);
	}
	
	public void deleteAuthor(int id) {
		authorDao.deleteById(id);
	}
	
	/*#########################################################################*/
	
	public boolean pubNameExists(String pubName) {
		List<Publisher> pub = publisherDao.findAll();
		
		for(Publisher p : pub) {
			if(p.getPublisherName().equals(pubName))
				return true;
		}
		return false;
	}
	
	public void addPublisher(Publisher p) {
		Publisher pub = new Publisher();
		pub.setPublisherName(p.getPublisherName());
		pub.setPublisherAddress(p.getPublisherAddress());
		pub.setPublisherPhone(p.getPublisherPhone());

		publisherDao.save(pub);
		System.out.println("Add Publisher Success!");
	}
	
	public Publisher getPublisher(int pubId) {
		Optional<Publisher> publisher;
		publisher = publisherDao.findById(pubId);
		return publisher.isPresent() ? publisher.get() : null;
	}
	
	public List<Publisher> getAllPublishers() {
		List<Publisher> pubs = null;
		pubs = publisherDao.findAll();
		return pubs;
	}
	
	public void updatePublisher(int pubId, Publisher p) {
		Publisher pub = new Publisher();
		pub.setPublisherId(pubId);
		pub.setPublisherName(p.getPublisherName());
		pub.setPublisherAddress(p.getPublisherAddress());
		pub.setPublisherPhone(p.getPublisherPhone());

		publisherDao.saveAndFlush(pub);
		System.out.println("Update Publisher Success!");
	}
	
	public void deletePublisher(int pubId) {
		Publisher pub = new Publisher();
		pub.setPublisherId(pubId);
		
		publisherDao.deleteById(pubId);
		System.out.println("Delete Publisher Success!");
	}
	
	/*#########################################################################*/
	
	public boolean branchNameExists(String branchName) {
		List<LibraryBranch> libBranch = null;
		libBranch = libraryBranchDao.findAll();
		
		for(LibraryBranch lb : libBranch) {
			if(lb.getBranchName().equals(branchName))
				return true;
		}
		return false;
	}
	
	public boolean branchInfoExists(String branchName, String branchAddr) {
		List<LibraryBranch> libBranches = null;
		libBranches = libraryBranchDao.findAll();
		
		for(LibraryBranch lb : libBranches) {
			if(lb.getBranchName().equals(branchName) && lb.getBranchAddress().equals(branchAddr))
				return true;
		}
		return false;
	}
	
	public List<LibraryBranch> getAllBranches() {
		return libraryBranchDao.findAll();
	}
	
	public LibraryBranch getLibraryBranch(int id) {
		Optional<LibraryBranch> libraryBranch = libraryBranchDao.findById(id);
		if(libraryBranch.isPresent())
			return libraryBranch.get();
		return null;
	}
	
	public LibraryBranch addLibraryBranch(LibraryBranch libraryBranch) {
		Optional<LibraryBranch> existingLibraryBranch = libraryBranchDao.findById(libraryBranch.getBranchId());
		if(!existingLibraryBranch.isPresent()) 
			return libraryBranchDao.save(libraryBranch);
		return null;
	}
	
	public LibraryBranch updateLibraryBranch(int id, LibraryBranch libraryBranch) {
		Optional<LibraryBranch> existingLibraryBranch = libraryBranchDao.findById(id);
		if(!existingLibraryBranch.isPresent())
			return null;
		libraryBranch.setBranchId(id);
		return libraryBranchDao.save(libraryBranch);
	}
	
	public void deleteLibraryBranch(int id) {
		libraryBranchDao.deleteById(id);
	}
	
	/*#########################################################################*/
	
	public boolean borrowerNameExists(String borrowerName) {
		List<Borrower> borrs = null;
		borrs = borrowerDao.findAll();
		
		for(Borrower borr : borrs) {
			if(borr.getName().equals(borrowerName))
				return true;
		}
		return false;
	}
	
	public List<Borrower> getAllBorrowers() {
		return borrowerDao.findAll();
	}
	
	public Borrower getBorrower(int id) {
		Optional<Borrower> borrower = borrowerDao.findById(id);
		if(borrower.isPresent())
			return borrower.get();
		return null;
	}
	
	public Borrower addBorrower(Borrower borrower) {
		Optional<Borrower> existingBorrower = borrowerDao.findById(borrower.getCardNo());
		if(!existingBorrower.isPresent()) 
			return borrowerDao.save(borrower);
		return null;
	}
	
	public Borrower updateBorrower(int id, Borrower borrower) {
		Optional<Borrower> existingBorrower = borrowerDao.findById(id);
		if(!existingBorrower.isPresent())
			return null;
		borrower.setCardNo(id);
		return borrowerDao.save(borrower);
	}
	
	public void deleteBorrower(int id) {
		borrowerDao.deleteById(id);
	}
	
	/*#########################################################################*/	
	
	public List<BookLoans> getAllBookLoans() {
		List<BookLoans> bookLoans = null;
		bookLoans = bookLoansDao.findAll();
		return bookLoans;
	}
	
	public void changeDueDate(int bookId, int branchId, int cardNo, Date dateOut, Date dueDate) {
		BookLoans bl = new BookLoans(bookId, branchId, cardNo, dateOut, dueDate);
		bookLoansDao.saveAndFlush(bl);;
		System.out.println("Update BookLoan Success!");
	}
}