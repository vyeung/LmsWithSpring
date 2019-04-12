package com.st.lms.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.st.lms.dao.GenericDao;
import com.st.lms.models.Book;

public class BookDaoImp implements GenericDao<Book> {

	private Connection con;
	
	public BookDaoImp(Connection con) {
		this.con = con;
	}
	
	@Override
	public boolean add(Book obj) throws SQLException {
		if(has(obj.getBookId()))
			return false;
		String query = "INSERT INTO tbl_book (title, authId, pubId) VALUES (?,?,?)";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getTitle());
		pstmt.setInt(2, obj.getAuthorId());
		pstmt.setInt(3, obj.getPubId());
		pstmt.executeUpdate();
		return true;
	}

	@Override
	public Book get(int objId) throws SQLException {
		Book book = null;
		String query = "SELECT * " +
					   "FROM tbl_book " +
					   "WHERE bookId=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId);	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			int bookId = rs.getInt("bookId");
			String title = rs.getString("title");
			int authId = rs.getInt("authId");
			int pubId = rs.getInt("pubId");
			book = new Book(bookId, title, authId, pubId);
		}	
		
		return book;
	}

	@Override
	public ArrayList<Book> getAll() throws SQLException {
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM tbl_book";
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			int bookId = rs.getInt("bookId");
			String title = rs.getString("title");
			int authId = rs.getInt("authId");
			int pubId = rs.getInt("pubId");
			Book b = new Book(bookId, title, authId, pubId);
			books.add(b);
		}	
		
		return books;
	}

	@Override
	public boolean update(Book obj) throws SQLException {
		if(!has(obj.getBookId()))
			return false;
		String query = "UPDATE tbl_book " + 
					   "SET title=?, authId=?, pubId=? " +
			           "WHERE bookId=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getTitle());
		pstmt.setInt(2, obj.getAuthorId());
		pstmt.setInt(3, obj.getPubId());
		pstmt.setInt(4, obj.getBookId());
		pstmt.executeUpdate();
		return true;
	}

	@Override
	public boolean delete(Book obj) throws SQLException {
		if(!has(obj.getBookId()))
			return false;
		String query = "DELETE FROM tbl_book WHERE bookId=?";
	
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, obj.getBookId());
		pstmt.executeUpdate();	
		return true;
	}
	
	@Override
	public boolean has(int objId) throws SQLException {
		String query = "SELECT * FROM tbl_book WHERE bookId=?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId);
		ResultSet rs = pstmt.executeQuery();
		return (rs.next());
	}
}