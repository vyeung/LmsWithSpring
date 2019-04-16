package com.st.lms.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.lms.dao.GenericDao;
import com.st.lms.daoImp.AuthorDaoImp;
import com.st.lms.daoImp.BookCopiesDaoImp;
import com.st.lms.daoImp.BookDaoImp;
import com.st.lms.daoImp.BookLoansDaoImp;
import com.st.lms.daoImp.BorrowerDaoImp;
import com.st.lms.daoImp.LibBranchDaoImp;
import com.st.lms.dto.BkCopiesDTO;
import com.st.lms.dto.BkLoansBkAuthDTO;
import com.st.lms.dto.BkLoansBranchDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookCopies;
import com.st.lms.models.BookCopiesPrimaryKey;
import com.st.lms.models.BookLoans;
import com.st.lms.models.Borrower;
import com.st.lms.models.LibraryBranch;
import com.st.lms.utils.ConnectionFactory;
import com.st.lms.utils.DateCalculations;

@Service
public class BorrowerService {
	
	private Connection con = ConnectionFactory.getMyConnection();
	
	@Autowired
	private BookDaoImp bookDao;
	@Autowired
	private BookLoansDaoImp bookLoansDao;
	@Autowired
	private BookCopiesDaoImp bookCopiesDao;
	@Autowired
	private BorrowerDaoImp borrowerDao;
	@Autowired
	private LibBranchDaoImp libBranchDao;
	@Autowired
	private AuthorDaoImp authorDao;
	
	
	public boolean borCardNoExists(int cardNo) {
		boolean flag;
		Borrower bor = null;
		try {
			bor = borrowerDao.findById(cardNo).get();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Problem with checking card number!");
			myRollBack();
		}
		
		if(bor.getName() == null) 
			flag = false;
		else 
			flag = true;
		
		return flag;
	}
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		try {
			libBranches = libBranchDao.findAll();
			con.commit();
		} catch (SQLException e) {
			System.out.println("Get All Branches Failed! :(");
			myRollBack();
		}
		return libBranches;
	}
	
	//to be implemented after hibernate
	public boolean branchExists(int branchId) {
		return false;
	}
	
	//to be implemented after hibernate
	public boolean loanExists(int cardNo, int branchId, int bookId) {
		return false;
	}
	//returns all bookCopies>=1 specific to a branch with book and author
	public List<BkCopiesDTO> getBkCopiesGreater1BookAndTitle(int branchId) {
		List<BkCopiesDTO> list = null;
		try {
			List<BookCopies> bookCopies = bookCopiesDao.findAll();
			Book book;
			Author author;
			
			BkCopiesDTO obj;
			list= new ArrayList<>();
			
			for(BookCopies bc : bookCopies) {			
				if(bc.getBranchId()==branchId && bc.getNoOfCopies()>=1) {
					//kind of like doing joins
					book = bookDao.findById(bc.getBookId()).get();
					author = authorDao.findById(book.getAuthorId()).get();
					
					obj = new BkCopiesDTO(bc, book, author);
					list.add(obj);
				}
			}
			con.commit();
		} 
		catch(SQLException e) {
			System.err.println("Unable to load books from the branch!");
			myRollBack();
		}
		
		return list;
	}
	
	public void checkOutBook(int bookId, int branchId, int cardNo, int noOfCopies) {
		Date dateOut, dueDate;
		dateOut = (Date) DateCalculations.getCurrentTime();
		dueDate = (Date) DateCalculations.getTodayPlus7();
		BookLoans bl = new BookLoans(bookId, branchId, cardNo, dateOut, dueDate);
		BookCopies bc = new BookCopies(bookId, branchId, noOfCopies-1);
		try {
			bookLoansDao.save(bl);           //add an entry to book_loans
			bookCopiesDao.saveAndFlush(bc);  //update noOfCopies with 1 less of that book
			con.commit();
			System.out.println("Book Checked Out!");
		} catch (SQLException e) {
			System.out.println("Unable to check out book!");
			myRollBack();
		}
	}
	
	/*##############################################################*/
	
	//returns the branches that user has a book checked out from based on their cardNo
	public List<BkLoansBranchDTO> getBranchesWithBkLoans(int cardNo) {
		List<BkLoansBranchDTO> list = null;
		try {
			List<BookLoans> bookLoans = bookLoansDao.findAll();
			LibraryBranch libBranch;
			
			BkLoansBranchDTO obj;
			list = new ArrayList<>();
			
			for(BookLoans bl : bookLoans) {
				libBranch = libBranchDao.findById(bl.getBranchId()).get();
				
				if(bl.getCardNo()==cardNo && bl.getDateOut()!=null && bl.getDueDate()!=null) {
					obj = new BkLoansBranchDTO(bl, libBranch);
					list.add(obj);
				}
			}
			
			//remove duplicates based on branch name
			HashSet<String> seen = new HashSet<>();
			list.removeIf(e -> !seen.add(e.getLibBranch().getBranchName()));
			
			con.commit();
		} 
		catch(SQLException e) {
			System.err.println("Unable to load branches that you want to return a book to!");
			myRollBack();
		}
		
		return list;
	}
	
	//returns the books that user has checked out within a specific branch  
	public List<BkLoansBkAuthDTO> getBooksFromBranch(int cardNo, int branchId) {
		List<BkLoansBkAuthDTO> list = null;
		try {
			List<BookLoans> bookLoans = bookLoansDao.findAll();
			Book book;
			Author author;
			
			BkLoansBkAuthDTO obj;
			list = new ArrayList<>();
			
			for(BookLoans bl : bookLoans) {
				if(bl.getCardNo()==cardNo && bl.getBranchId()==branchId) {
					book = bookDao.findById(bl.getBookId()).get();
					author = authorDao.findById(book.getAuthorId()).get();

					obj = new BkLoansBkAuthDTO(bl, book, author);
					list.add(obj);
				}
			}
			con.commit();
		} 
		catch(SQLException e) {
			System.err.println("Unable to load the books you want to return!");
			myRollBack();
		}
		
		return list;
	}
	
	public int getNoOfCopies(int bookId, int branchId) {
		int numOfCopies = 0;
		
		BookCopiesPrimaryKey bcpk = new BookCopiesPrimaryKey(bookId, branchId);
		BookCopies bc = bookCopiesDao.findById(bcpk).get();
		numOfCopies = bc.getNoOfCopies();
		
		return numOfCopies;
	}
	
	public void returnBook(int bookId, int branchId, int cardNo, int noOfCopies) {
		BookLoans bl = new BookLoans();
		bl.setBookId(bookId);
		bl.setBranchId(branchId);
		bl.setCardNo(cardNo);
		
		BookCopies bc = new BookCopies();
		bc.setBookId(bookId);
		bc.setBranchId(branchId);
		bc.setNoOfCopies(noOfCopies+1);
		
		try {
			bookLoansDao.delete(bl);        //delete entry in book loans
			bookCopiesDao.saveAndFlush(bc); //update noOfCopies with 1 more of that book
			con.commit();
			System.out.println("Book Returned!");
		} catch (SQLException e) {
			System.out.println("Unable to return book!");
			myRollBack();
		}
	}
	
	private void myRollBack() {
		try {
			con.rollback();
			System.out.println("Rolling Back...");
		} catch (SQLException e) {
			System.out.println("Unable to Roll Back!");
		}
	}
}