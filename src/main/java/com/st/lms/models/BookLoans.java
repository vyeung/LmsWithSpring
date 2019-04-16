package com.st.lms.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@IdClass(BookLoansPrimaryKey.class)
@Entity
@Table(name = "tbl_book_loans")
public class BookLoans {
	
	@Id
	@Column(name = "bookId")
	private Integer bookId;
	
	@Id
	@Column(name = "branchId")
	private Integer branchId;
	
	@Id
	@Column(name = "cardNo")
	private Integer cardNo;
	
	@Column(name = "dateOut")
	private Date dateOut;
	
	@Column(name = "dueDate")
	private Date dueDate;
	
	
	public BookLoans() {}
	
	public BookLoans(Integer bookId, Integer branchId, Integer cardNo, Date dateOut, Date dueDate) {
		this.bookId = bookId;
		this.branchId = branchId;
		this.cardNo = cardNo;
		this.dateOut = dateOut;
		this.dueDate = dueDate;
	}
	
	public int getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "BookLoans [bookId=" + bookId + ", branchId=" + branchId + ", cardNo=" + cardNo + ", dateOut=" + dateOut
				+ ", dueDate=" + dueDate + "]\n";
	}
}