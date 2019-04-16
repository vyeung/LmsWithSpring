package com.st.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	private GenericDao<LibraryBranch> genDaoLibBranch = new LibBranchDaoImp(con);
	
	@Autowired
	private BookDaoImp bookDao;
	
	private GenericDao<Author> genDaoAuthor = new AuthorDaoImp(con);
	
	@Autowired
	private BookCopiesDaoImp bookCopiesDao;
	
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		try {
			libBranches = genDaoLibBranch.getAll();
			con.commit();
		} catch (SQLException e) {
			myRollBack();
		}
		return libBranches;
	}
	
	public LibraryBranch getLibraryBranch(int branchId) {
		try {
			return genDaoLibBranch.get(branchId);
		} catch(SQLException e) {
			return null;
		}
	}
	
	public void updateBranch(int branchId, String branchName, String branchAddr) {
		LibraryBranch libBranch = new LibraryBranch(branchId, branchName, branchAddr);
		try {
			genDaoLibBranch.update(libBranch);
			con.commit();
		} catch (SQLException e) {
			myRollBack();
		}
	}
	
	//returns all bookCopies specific to a branch with book and author
	public List<BkCopiesDTO> getBookCopiesBookAndTitle(int branchId) {
		List<BkCopiesDTO> list = null;
		try {
			List<BookCopies> bookCopies = bookCopiesDao.findAll();
			Book book;
			Author author;
			
			BkCopiesDTO obj;
			list= new ArrayList<>();
			
			for(BookCopies bc : bookCopies) {
				if(bc.getBranchId() == branchId) {
					//kind of like doing joins
					book = bookDao.findById(bc.getBookId()).get();
					author = genDaoAuthor.get(book.getAuthorId());
					
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
		bookCopiesDao.saveAndFlush(bc);
		System.out.println("Update Book Copies Success!");
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