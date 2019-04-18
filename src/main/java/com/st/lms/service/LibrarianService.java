package com.st.lms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.lms.dao.AuthorDao;
import com.st.lms.dao.BookCopiesDao;
import com.st.lms.dao.BookDao;
import com.st.lms.dao.LibBranchDao;
import com.st.lms.dto.BkCopiesDTO;
import com.st.lms.models.Author;
import com.st.lms.models.Book;
import com.st.lms.models.BookCopies;
import com.st.lms.models.LibraryBranch;

@Service
public class LibrarianService {
	
	@Autowired
	private LibBranchDao libBranchDao;
	@Autowired
	private AuthorDao authorDao;
	@Autowired
	private BookDao bookDao;
	@Autowired
	private BookCopiesDao bookCopiesDao;
	
	public List<LibraryBranch> getAllBranches() {
		List<LibraryBranch> libBranches = null;
		libBranches = libBranchDao.findAll();
		return libBranches;
	}
	
	public Book getBook(int bookId) {
		Optional<Book> book;
		book = bookDao.findById(bookId);
		return book.isPresent() ? book.get() : null;
	}
	
	public LibraryBranch getLibraryBranch(int branchId) {
		Optional<LibraryBranch> libBranch;
		libBranch = libBranchDao.findById(branchId);
		return libBranch.isPresent() ? libBranch.get() : null;
	}
	
	public void updateBranch(int branchId, String branchName, String branchAddr) {
		LibraryBranch libBranch = new LibraryBranch(branchId, branchName, branchAddr);
		libBranchDao.save(libBranch);
		System.out.println("Update Library Branch Success!");
	}
	
	//returns all bookCopies specific to a branch with book and author
	public List<BkCopiesDTO> getBookCopiesBookAndTitle(int branchId) {
		List<BkCopiesDTO> list = null;
		List<BookCopies> bookCopies = bookCopiesDao.findAll();
		Book book;
		Author author;
		
		BkCopiesDTO obj;
		list= new ArrayList<>();
		
		for(BookCopies bc : bookCopies) {
			if(bc.getBranchId() == branchId) {
				//kind of like doing joins
				book = bookDao.findById(bc.getBookId()).get();
				author = authorDao.findById(book.getAuthorId()).get();
				
				obj = new BkCopiesDTO(bc, book, author);
				list.add(obj);
			}
		}
		return list;
	}
	
	public void updateNumCopies(int bookId, int branchId, int numCopies) {
		BookCopies bc = new BookCopies(bookId, branchId, numCopies);
		bookCopiesDao.saveAndFlush(bc);
		System.out.println("Update Book Copies Success!");
	}
}