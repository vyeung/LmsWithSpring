package com.st.lms.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.st.lms.dao.GenericDao;
import com.st.lms.models.Author;

public class AuthorDaoImp implements GenericDao<Author> {
	
	private Connection con;
	
	public AuthorDaoImp(Connection con) {
		this.con = con;
	}

	/*
	 * (non-Javadoc)
	 * @see com.st.lms.dao.GenericDao#add(java.lang.Object)
	 * returns false if an object with given id# already exists
	 */
	@Override
	public boolean add(Author obj) throws SQLException {
		if(has(obj.getAuthorId()))
			return false;
		String query = "INSERT INTO tbl_author (authorName) VALUES (?)";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getAuthorName());
		pstmt.executeUpdate();
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.st.lms.dao.GenericDao#get(int)
	 * returns null if object is not found
	 */
	@Override
	public Author get(int objId) throws SQLException {
		Author author = null;
		String query = "SELECT * FROM tbl_author WHERE authorId=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId); //the first question mark
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			int authId = rs.getInt("authorId");
			String authName = rs.getString("authorName");
			author = new Author(authId, authName);
		}		
		
		return author;
	}

	/*
	 * (non-Javadoc)
	 * @see com.st.lms.dao.GenericDao#getAll()
	 * returns empty list if nothing is in the table
	 */
	@Override
	public ArrayList<Author> getAll() throws SQLException {
		ArrayList<Author> authors = new ArrayList<>();
		String query = "SELECT * FROM tbl_author";

		PreparedStatement pstmt = con.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			int authId = rs.getInt("authorId");
			String authName = rs.getString("authorName");
			Author a = new Author(authId, authName);
			authors.add(a);
		}		
		
		return authors;
	}

	/*
	 * (non-Javadoc)
	 * @see com.st.lms.dao.GenericDao#update(java.lang.Object)
	 * returns false if the object does not exist
	 */
	@Override
	public boolean update(Author obj) throws SQLException {
		if(!has(obj.getAuthorId()))
			return false;
		String query = "UPDATE tbl_author SET authorName=? WHERE authorId=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getAuthorName());
		pstmt.setInt(2, obj.getAuthorId());
		pstmt.executeUpdate();
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.st.lms.dao.GenericDao#delete(java.lang.Object)
	 * returns false if the object does not exist
	 */
	@Override
	public boolean delete(Author obj) throws SQLException {
		if(!has(obj.getAuthorId()))
			return false;
		String query = "DELETE FROM tbl_author WHERE authorId=?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, obj.getAuthorId());
		pstmt.executeUpdate();
		return true;
	}
	
	@Override
	public boolean has(int objId) throws SQLException {
		String query = "SELECT * FROM tbl_author WHERE authorId=?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId);
		ResultSet rs = pstmt.executeQuery();
		return (rs.next());
	}
}