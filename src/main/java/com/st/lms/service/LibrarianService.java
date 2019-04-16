package com.st.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.lms.dao.BookCopiesDao;
import com.st.lms.dao.GenericDao;
import com.st.lms.daoImp.AuthorDaoImp;
import com.st.lms.daoImp.BookCopiesDaoImp;
import com.st.lms.daoImp.BookDaoImp;
import com.st.lms.daoImp.LibBranchDaoImp;
import com.st.lms.dto.BkCopiesDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookCopies;
import com.st.lms.models.LibraryBranch;
import com.st.lms.utils.ConnectionFactory;

@Service
public class LibrarianService {
	
	private Connection con = ConnectionFactory.getMyConnection();
	
	@Autowired
	private LibBranchDaoImp genDaoLibBranch;
	private GenericDao<Book> genDaoBook = new BookDaoImp(con);
	@Autowired
	private AuthorDaoImp genDaoAuthor;
	private BookCopiesDao bookCopiesDao = new BookCopiesDaoImp(con);
	
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		try {
			libBranches = genDaoLibBranch.findAll();
			con.commit();
		} catch (SQLException e) {
			myRollBack();
		}
		return libBranches;
	}
	
	public LibraryBranch getLibraryBranch(int branchId) {
		return genDaoLibBranch.findById(branchId).get();
	}
	
	public void updateBranch(int branchId, String branchName, String branchAddr) {
		LibraryBranch libBranch = new LibraryBranch(branchId, branchName, branchAddr);
		try {
			genDaoLibBranch.save(libBranch);
			con.commit();
		} catch (SQLException e) {
			myRollBack();
		}
	}
	
	//returns all bookCopies specific to a branch with book and author
	public List<BkCopiesDTO> getBookCopiesBookAndTitle(int branchId) {
		List<BkCopiesDTO> list = null;
		try {
			List<BookCopies> bookCopies = bookCopiesDao.getAll();
			Book book;
			Author author;
			
			BkCopiesDTO obj;
			list= new ArrayList<>();
			
			for(BookCopies bc : bookCopies) {
				if(bc.getBranchId() == branchId) {
					//kind of like doing joins
					book = genDaoBook.get(bc.getBookId());
					author = genDaoAuthor.findById(book.getAuthorId()).get();
					
					obj = new BkCopiesDTO(bc, book, author);
					list.add(obj);
				}
			}
			con.commit();
		} 
		catch(SQLException e) {
			System.out.println("Unable to load the book copies of your branch!");
			myRollBack();
		}
		
		return list;
	}
	
	public void updateNumCopies(int bookId, int branchId, int numCopies) {
		BookCopies bc = new BookCopies(bookId, branchId, numCopies);
		try {
			bookCopiesDao.update(bc);
			con.commit();
			System.out.println("Update Book Copies Success!");
		} catch (SQLException e) {
			System.out.println("Unable to update copies!");
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