package com.st.lms.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.st.lms.dao.GenericDao;
import com.st.lms.models.Borrower;

public class BorrowerDaoImp implements GenericDao<Borrower> {

	private Connection con;
	
	public BorrowerDaoImp(Connection con) {
		this.con = con;
	}
	
	@Override
	public boolean add(Borrower obj) throws SQLException {
		if(has(obj.getCardNo()))
			return false;
		String query = "INSERT INTO tbl_borrower (name, address, phone) VALUES (?,?,?)";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getName());
		pstmt.setString(2, obj.getAddress());
		pstmt.setString(3, obj.getPhone());
		pstmt.executeUpdate();
		return true;
	}

	@Override
	public Borrower get(int objId) throws SQLException {
		Borrower borrower = null;
		String query = "SELECT * FROM tbl_borrower WHERE cardNo=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			int cardNo = rs.getInt("cardNo");
			String name = rs.getString("name");
			String addr = rs.getString("address");
			String phone = rs.getString("phone");
			borrower = new Borrower(cardNo, name, addr, phone);
		}		
		
		return borrower;
	}

	@Override
	public ArrayList<Borrower> getAll() throws SQLException {
		ArrayList<Borrower> borrower = new ArrayList<>();
		String query = "SELECT * FROM tbl_borrower";

		PreparedStatement pstmt = con.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			int cardNo = rs.getInt("cardNo");
			String name = rs.getString("name");
			String addr = rs.getString("address");
			String phone = rs.getString("phone");
			Borrower borr = new Borrower(cardNo, name, addr, phone);
			borrower.add(borr);
		}	

		return borrower;
	}

	@Override
	public boolean update(Borrower obj) throws SQLException {
		if(!has(obj.getCardNo()))
			return false;
		String query = "UPDATE tbl_borrower " + 
			       	   "SET name=?, address=?, phone=? " +
			           "WHERE cardNo=?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, obj.getName());
		pstmt.setString(2, obj.getAddress());
		pstmt.setString(3, obj.getPhone());
		pstmt.setInt(4, obj.getCardNo());
		pstmt.executeUpdate();
		return true;
	}

	@Override
	public boolean delete(Borrower obj) throws SQLException {
		if(!has(obj.getCardNo()))
			return false;
		String query = "DELETE FROM tbl_borrower WHERE cardNo=?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, obj.getCardNo());
		pstmt.executeUpdate();
		return true;
	}
	
	@Override
	public boolean has(int objId) throws SQLException {
		String query = "SELECT * FROM tbl_borrower WHERE cardNo=?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, objId);
		ResultSet rs = pstmt.executeQuery();
		return (rs.next());
	}
}