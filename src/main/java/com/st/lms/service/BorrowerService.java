package com.st.lms.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.st.lms.dao.AuthorDao;
import com.st.lms.dao.BookCopiesDao;
import com.st.lms.dao.BookDao;
import com.st.lms.dao.BookLoansDao;
import com.st.lms.dao.BorrowerDao;
import com.st.lms.dao.LibBranchDao;
import com.st.lms.dto.BkCopiesDTO;
import com.st.lms.dto.BkLoansBkAuthDTO;
import com.st.lms.dto.BkLoansBranchDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookCopies;
import com.st.lms.models.BookCopiesPrimaryKey;
import com.st.lms.models.BookLoans;
import com.st.lms.models.BookLoansPrimaryKey;
import com.st.lms.models.LibraryBranch;
import com.st.lms.utils.DateCalculations;

@Service
public class BorrowerService {
	
	@Autowired
	private BookDao bookDao;
	@Autowired
	private BookLoansDao bookLoansDao;
	@Autowired
	private BookCopiesDao bookCopiesDao;
	@Autowired
	private BorrowerDao borrowerDao;
	@Autowired
	private LibBranchDao libBranchDao;
	@Autowired
	private AuthorDao authorDao;
	
	public boolean cardNoExists(int cardNo) {
		return borrowerDao.findById(cardNo).isPresent();
	}
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		libBranches = libBranchDao.findAll();
		return libBranches;
	}
	
	public boolean branchExists(int branchId) {
		return libBranchDao.findById(branchId).isPresent();
	}
	
	public boolean loanExists(int cardNo, int branchId, int bookId) {
		return bookLoansDao.findById(new BookLoansPrimaryKey(bookId, branchId, cardNo)).isPresent();
	}
	
	//returns all bookCopies>=1 specific to a branch with book and author
	public List<BkCopiesDTO> getBkCopiesGreater1BookAndTitle(int branchId) {
		List<BkCopiesDTO> list = null;
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
		return list;
	}
	
	@Transactional
	public void checkOutBook(int bookId, int branchId, int cardNo, int noOfCopies) {
		Date dateOut, dueDate;
		dateOut = (Date) DateCalculations.getCurrentTime();
		dueDate = (Date) DateCalculations.getTodayPlus7();
		BookLoans bl = new BookLoans(bookId, branchId, cardNo, dateOut, dueDate);
		BookCopies bc = new BookCopies(bookId, branchId, noOfCopies-1);
		bookLoansDao.save(bl);           //add an entry to book_loans
		bookCopiesDao.saveAndFlush(bc);  //update noOfCopies with 1 less of that book
		System.out.println("Book checked out!");
	}
	
	/*##############################################################*/
	
	//returns a list of BkLoansBkAuth DTOs of the given cardNo{
		public List<BkLoansBkAuthDTO> getBorrowedBooks(int cardNo){
			List<BkLoansBkAuthDTO> loans = new ArrayList<BkLoansBkAuthDTO>();
			List<BookLoans> allLoans = bookLoansDao.findAll();
			Book book;
			Author author;
			for (BookLoans loan : allLoans) {
				if(loan.getCardNo()==cardNo) {
					book = bookDao.findById(loan.getBookId()).get();
					author = authorDao.findById(book.getAuthorId()).get();
					loans.add(new BkLoansBkAuthDTO(loan, book, author));
				}
			}
			return loans;
		}
		
	//returns the branches that user has a book checked out from based on their cardNo
	public List<BkLoansBranchDTO> getBranchesWithBkLoans(int cardNo) {
		List<BkLoansBranchDTO> list = new ArrayList<>();
		List<BookLoans> bookLoans = bookLoansDao.findAll();
		LibraryBranch libBranch;
		
		BkLoansBranchDTO obj;
		
		for(int i = 0; i < bookLoans.size(); i++) {
			if(bookLoans.get(i).getCardNo() == cardNo) {
				libBranch = libBranchDao.findById(bookLoans.get(i).getBranchId()).get();
				obj = new BkLoansBranchDTO(libBranch, new ArrayList<BookLoans>());
				for(int j = i; j < bookLoans.size(); j++) {
					if(bookLoans.get(j).getBranchId()==libBranch.getBranchId() && bookLoans.get(j).getCardNo()==cardNo) {
						obj.addBookLoan(bookLoans.get(j));
						bookLoans.remove(j);
						j--;
					}
				}
				list.add(obj);
			}
		}
		return list;
	}
	
	//returns the books that user has checked out within a specific branch  
	public List<BkLoansBkAuthDTO> getBooksFromBranch(int cardNo, int branchId) {
		List<BkLoansBkAuthDTO> list = null;
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
		return list;
	}
	
	public int getNoOfCopies(int bookId, int branchId) {
		int numOfCopies = 0;
		BookCopiesPrimaryKey bcpk = new BookCopiesPrimaryKey(bookId, branchId);
		BookCopies bc = bookCopiesDao.findById(bcpk).get();
		numOfCopies = bc.getNoOfCopies();
		
		return numOfCopies;
	}
	
	@Transactional
	public void returnBook(int bookId, int branchId, int cardNo, int noOfCopies) {
		BookLoans bl = new BookLoans();
		bl.setBookId(bookId);
		bl.setBranchId(branchId);
		bl.setCardNo(cardNo);
		
		BookCopies bc = new BookCopies();
		bc.setBookId(bookId);
		bc.setBranchId(branchId);
		bc.setNoOfCopies(noOfCopies+1);
		
		bookLoansDao.delete(bl);        //delete entry in book loans
		bookCopiesDao.saveAndFlush(bc); //update noOfCopies with 1 more of that book
		System.out.println("Book returned!");
	}
}