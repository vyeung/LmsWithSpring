package com.st.lms.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.lms.dao.BookLoansDao;
import com.st.lms.dao.GenericDao;
import com.st.lms.daoImp.AuthorDaoImp;
import com.st.lms.daoImp.BookDaoImp;
import com.st.lms.daoImp.BookLoansDaoImp;
import com.st.lms.daoImp.BorrowerDaoImp;
import com.st.lms.daoImp.LibBranchDaoImp;
import com.st.lms.daoImp.PublisherDaoImp;
import com.st.lms.dto.BkAuthPubDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookLoans;
import com.st.lms.models.Borrower;
import com.st.lms.models.LibraryBranch;
import com.st.lms.models.Publisher;
import com.st.lms.utils.ConnectionFactory;

@Service
public class AdminService {
	
	Connection con = ConnectionFactory.getMyConnection();
	
	@Autowired
	private AuthorDaoImp authorDao;
	private GenericDao<Book> genDaoBook = new BookDaoImp(con);
	@Autowired
	private BorrowerDaoImp borrowerDao;
	@Autowired
	private LibBranchDaoImp libraryBranchDao;
	private GenericDao<Publisher> genDaoPublisher = new PublisherDaoImp(con);
	private BookLoansDao bookLoansDao = new BookLoansDaoImp(con);

	
	public boolean bookTitleExists(String bookTitle) {
		List<Book> books = null;
		try {
			books = genDaoBook.getAll();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Failure in bookTitleExists()");
			myRollBack();
		}
		
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
		try {
			genDaoBook.add(b);
			con.commit();
			System.out.println("Add Book Success!");

		} catch (SQLException e) {
			System.out.println("Add Book Failed! :(");
			myRollBack();
		}
	}
	
	public Book getBook(int bookId) {
		Book book = null;
		try {
			book = genDaoBook.get(bookId);
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get Book Failed! :(");	
			myRollBack();
		}
		return book;
	}
	
	public List<Book> getAllBooks() {
		List<Book> books = null;
		try {
			books = genDaoBook.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Books Failed! :(");
			myRollBack();
		}
		return books;
	}
	
	public BkAuthPubDTO getBookWithAuthAndPub(int bookId) {
		BkAuthPubDTO obj = null;
		try {
			List<Book> books = genDaoBook.getAll();
			Author author;
			Publisher publisher;
			
			for(Book b : books) {
				if(b.getBookId() == bookId) {
					author = authorDao.findById(b.getAuthorId()).get();
					publisher = genDaoPublisher.get(b.getPubId());
					
					obj = new BkAuthPubDTO(bookId, b.getTitle(), 
									       author.getAuthorName(), publisher.getPublisherName());
				}
			}
		}
		catch(SQLException e) {
			System.err.println("Unable to load book with author and publisher!");
		}
		
		return obj;
	}
	
	public List<BkAuthPubDTO> getBooksWithAuthAndPub() {
		List<BkAuthPubDTO> list = null;
		
		try {
			List<Book> books = genDaoBook.getAll();
			Author author;
			Publisher publisher;
			
			BkAuthPubDTO obj;
			list = new ArrayList<>();
			
			for(Book b : books) {
				author = authorDao.findById(b.getAuthorId()).get();
				publisher = genDaoPublisher.get(b.getPubId());
				
				obj = new BkAuthPubDTO(b.getBookId(), b.getTitle(), 
									   author.getAuthorName(), publisher.getPublisherName());
				list.add(obj);
			}
		}
		catch(SQLException e) {
			System.err.println("Unable to load books with author and publisher!");
		}
		
		return list;
	}
	
	public void updateBook(int bookId, Book b) {
		Book book = new Book();
		book.setBookId(bookId);
		book.setTitle(b.getTitle());
		book.setAuthorId(b.getAuthorId());
		book.setPubId(b.getPubId());
		try {
			genDaoBook.update(book);
			con.commit();
			System.out.println("Update Book Success!");
		} catch (SQLException e) {
			System.out.println("Update Book Failed! :(");
			myRollBack();
		}
	}
	
	public void deleteBook(int bookId) {
		Book b = new Book();
		b.setBookId(bookId);
		try {
			genDaoBook.delete(b);
			con.commit();
			System.out.println("Delete Book Success!");
		} catch (SQLException e) {
			System.out.println("Delete Book Failed! :(");
			myRollBack();
		}
	}
	
	/*#########################################################################*/
	
	public boolean authNameExists(String authName) {
		List<Author> authors = null;
		try {
			authors = authorDao.findAll();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Failure in authNameExists()");
			myRollBack();
		}
		
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
		return authorDao.save(author);
	}
	
	public void deleteAuthor(int id) {
		
		authorDao.deleteById(id);
	}
	
	/*#########################################################################*/
	
	public boolean pubNameExists(String pubName) {
		List<Publisher> pub = null;
		try {
			pub = genDaoPublisher.getAll();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Failure in pubNameExists()");
			myRollBack();
		}
		
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
		try {
			genDaoPublisher.add(pub);
			con.commit();
			System.out.println("Add Publisher Success!");
		} catch (SQLException e) {
			System.out.println("Add Publisher Failed! :(");	
			myRollBack();
		}
	}
	
	public Publisher getPublisher(int pubId) {
		Publisher publisher = null;
		try {
			publisher = genDaoPublisher.get(pubId);
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get Publisher Failed! :(");	
			myRollBack();
		}
		return publisher;
	}
	
	public List<Publisher> getAllPublishers() {
		List<Publisher> pubs = null;
		try {
			pubs = genDaoPublisher.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Publishers Failed! :( ");
			myRollBack();
		}
		return pubs;
	}
	
	public void updatePublisher(int pubId, Publisher p) {
		Publisher pub = new Publisher();
		pub.setPublisherId(pubId);
		pub.setPublisherName(p.getPublisherName());
		pub.setPublisherAddress(p.getPublisherAddress());
		pub.setPublisherPhone(p.getPublisherPhone());
		try {
			genDaoPublisher.update(pub);
			con.commit();
			System.out.println("Update Publisher Success!");
		} catch (SQLException e) {
			System.out.println("Update Publisher Failed! :(");
			myRollBack();
		}
	}
	
	public void deletePublisher(int pubId) {
		Publisher pub = new Publisher();
		pub.setPublisherId(pubId);
		try {
			genDaoPublisher.delete(pub);
			con.commit();
			System.out.println("Delete Publisher Success!");
		} catch (SQLException e) {
			System.out.println("Delete Publisher Failed! :(");
			myRollBack();
		}
	}
	
	/*#########################################################################*/
	
	public boolean branchNameExists(String branchName) {
		List<LibraryBranch> libBranch = null;
		try {
			libBranch = libraryBranchDao.findAll();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Failure in branchNameExists()");
			myRollBack();
		}
		
		for(LibraryBranch lb : libBranch) {
			if(lb.getBranchName().equals(branchName))
				return true;
		}
		return false;
	}
	
	public boolean branchInfoExists(String branchName, String branchAddr) {
		List<LibraryBranch> libBranches = null;
		try {
			libBranches = libraryBranchDao.findAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Failure in branchInfoExists");
			myRollBack();
		}
		
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
		return libraryBranchDao.save(libraryBranch);
	}
	
	public void deleteLibraryBranch(int id) {
		
		libraryBranchDao.deleteById(id);
	}
	
	/*#########################################################################*/
	
	public boolean borrowerNameExists(String borrowerName) {
		List<Borrower> borrs = null;
		try {
			borrs = borrowerDao.findAll();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Failure in borrowerNameExists()");
			myRollBack();
		}
		
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
		return borrowerDao.save(borrower);
	}
	
	public void deleteBorrower(int id) {
		
		borrowerDao.deleteById(id);
	}
	
	/*#########################################################################*/	
	
	public List<BookLoans> getAllBookLoans() {
		List<BookLoans> bookLoans = null;
		try {
			bookLoans = bookLoansDao.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All BookLoans Failed! :(");
			myRollBack();
		}
		return bookLoans;
	}
	
	public void changeDueDate(int bookId, int branchId, int cardNo, Date dateOut, Date dueDate) {
		BookLoans bl = new BookLoans(bookId, branchId, cardNo, dateOut, dueDate);
		try {
			bookLoansDao.update(bl);
			con.commit();
			System.out.println("Update BookLoan Success!");
		} catch (SQLException e) {
			System.out.println("Update BookLoan Failed! :(");
			myRollBack();
		}
	}
	
	/*#########################################################################*/
	
	private void myRollBack() {
		try {
			con.rollback();
			System.out.println("Rolling Back...");
		} catch (SQLException e) {
			System.out.println("Unable to Roll Back!");
		}
	}
}