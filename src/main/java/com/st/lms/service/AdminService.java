package com.st.lms.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	private GenericDao<Author> genDaoAuthor = new AuthorDaoImp(con);
	
	@Autowired
	private BookDaoImp bookDao;
	
	private GenericDao<Borrower> genDaoBorrower = new BorrowerDaoImp(con);
	private GenericDao<LibraryBranch> genDaoLibBranch = new LibBranchDaoImp(con);
	
	@Autowired
	private PublisherDaoImp publisherDao;
	
	@Autowired
	private BookLoansDaoImp bookLoansDao;

	
	public boolean bookTitleExists(String bookTitle) {
		List<Book> books = null;
		books = bookDao.findAll();
		
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
		try {
			List<Book> books = bookDao.findAll();
			Author author;
			Publisher publisher;
			
			for(Book b : books) {
				if(b.getBookId() == bookId) {
					author = genDaoAuthor.get(b.getAuthorId());
					publisher = publisherDao.findById(b.getPubId()).get();
					
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
			List<Book> books = bookDao.findAll();
			Author author;
			Publisher publisher;
			
			BkAuthPubDTO obj;
			list = new ArrayList<>();
			
			for(Book b : books) {
				author = genDaoAuthor.get(b.getAuthorId());
				publisher = publisherDao.findById(b.getPubId()).get(); //.get(b.getPubId());
				
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
		List<Author> authors = null;
		try {
			authors = genDaoAuthor.getAll();
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
	
	public void addAuthor(String authName) {
		Author author = new Author();
		author.setAuthorName(authName);
		try {
			genDaoAuthor.add(author);
			con.commit();
			System.out.println("Add Author Success!");
		} catch (SQLException e) {
			System.out.println("Add Author Failed!");
			myRollBack();
		}
	}
	
	public Author getAuthor(int authId) {
		Author author = null;
		try {
			author = genDaoAuthor.get(authId);
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get Author Failed! :(");
			myRollBack();
		}
		return author;
	}
	
	public List<Author> getAllAuthors() {
		List<Author> authors = null;
		try {
			authors = genDaoAuthor.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Authors Failed! :(");
			myRollBack();
		}
		return authors;
	}	
	
	public void updateAuthor(int authId, String authName) {
		Author author = new Author(authId, authName);
		try {
			genDaoAuthor.update(author);
			con.commit();
			System.out.println("Update Author Success!");
		} catch (SQLException e) {
			System.out.println("Update Author Failed! :(");
			myRollBack();
		}
	}
	
	public void deleteAuthor(int authId) {
		Author author = new Author();
		author.setAuthorId(authId);
		try {
			genDaoAuthor.delete(author);
			con.commit();
			System.out.println("Delete Author Success!");
		} catch (SQLException e) {
			System.out.println("Delete Author Failed! :(");	
			myRollBack();
		}
	}
	
	/*#########################################################################*/
	
	public boolean pubNameExists(String pubName) {
		List<Publisher> pub = publisherDao.findAll();
		System.err.println("Failure in pubNameExists()");
		
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
		try {
			libBranch = genDaoLibBranch.getAll();
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
		List<LibraryBranch> libBranch = null;
		try {
			libBranch = genDaoLibBranch.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Failure in branchInfoExists");
			myRollBack();
		}
		
		for(LibraryBranch lb : libBranch) {
			if(lb.getBranchName().equals(branchName) && lb.getBranchAddress().equals(branchAddr))
				return true;
		}
		return false;
	}
	
	public void addLibraryBranch(String branchName, String branchAddr) {
		LibraryBranch lb = new LibraryBranch();
		lb.setBranchName(branchName);
		lb.setBranchAddress(branchAddr);
		try {
			genDaoLibBranch.add(lb);
			con.commit();
			System.out.println("Add Branch Success!");
		} catch (SQLException e) {
			System.out.println("Add Branch Failed! :(");
			myRollBack();
		}
	}
	
	public LibraryBranch getLibBranch(int branchId) {
		LibraryBranch libBranch = null;
		try {
			libBranch = genDaoLibBranch.get(branchId);
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get Branch Failed! :(");
			myRollBack();
		}
		return libBranch;
	}
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		try {
			libBranches = genDaoLibBranch.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Branches Failed! :(");
			myRollBack();
		}
		return libBranches;
	}
	
	public void updateLibraryBranch(int branchId, String branchName, String branchAddr) {
		LibraryBranch lb = new LibraryBranch(branchId, branchName, branchAddr);
		try {
			genDaoLibBranch.update(lb);
			con.commit();
			System.out.println("Update Branch Success!");
		} catch (SQLException e) {
			System.out.println("Update Branch Failed! :(");
			myRollBack();
		}
	}
	
	public void deleteLibraryBranch(int branchId) {
		LibraryBranch lb = new LibraryBranch();
		lb.setBranchId(branchId);
		try {
			genDaoLibBranch.delete(lb);
			con.commit();
			System.out.println("Delete Branch Success!");
		} catch (SQLException e) {
			System.out.println("Delete Branch Failed! :(");
			myRollBack();
		}
	}
	
	/*#########################################################################*/
	
	public boolean borrowerNameExists(String borrowerName) {
		List<Borrower> borrs = null;
		try {
			borrs = genDaoBorrower.getAll();
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
	
	public void addBorrower(String name, String addr, String phone) {
		Borrower borr = new Borrower();
		borr.setName(name);
		borr.setAddress(addr);
		borr.setPhone(phone);
		try {
			genDaoBorrower.add(borr);
			con.commit();
			System.out.println("Add Borrower Success!");
		} catch (SQLException e) {
			System.out.println("Add Borrower Failed! :(");
			myRollBack();
		}
	}
	
	public Borrower getBorrower(int borrId) {
		Borrower borrower = null;
		try {
			borrower = genDaoBorrower.get(borrId);
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get Borrower Failed! :(");
			myRollBack();
		}
		return borrower;
	}
	
	public List<Borrower> getAllBorrowers() {
		List<Borrower> borrowers = null;
		try {
			borrowers = genDaoBorrower.getAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Borrowers Failed! :(");
			myRollBack();
		}
		return borrowers;
	}
	
	public void updateBorrower(int cardNo, String name, String addr, String phone) {
		Borrower borr = new Borrower(cardNo, name, addr, phone);
		try {
			genDaoBorrower.update(borr);
			con.commit();
			System.out.println("Update Borrower Success!");
		} catch (SQLException e) {
			System.out.println("Update Borrower Failed! :(");
			myRollBack();
		}
	}
	
	public void deleteBorrower(int cardNo) {
		Borrower borr = new Borrower();
		borr.setCardNo(cardNo);
		try {
			genDaoBorrower.delete(borr);
			con.commit();
			System.out.println("Delete Borrower Success!");	
		} catch (SQLException e) {
			System.out.println("Delete Borrower Failed! :(");
			myRollBack();
		}
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