package com.st.lms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@IdClass(BookCopiesPrimaryKey.class)
@Entity
@Table(name = "tbl_book_copies")
public class BookCopies {
	
	@Id
	@Column(name = "bookId")
	private Integer bookId;
	
	@Id
	@Column(name = "branchId")
	private Integer branchId;
	
	@Column(name = "noOfCopies")
	private Integer noOfCopies;
	
	
	public BookCopies() {}
	
	public BookCopies(Integer bookId, Integer branchId, Integer noOfCopies) {
		this.bookId = bookId;
		this.branchId = branchId;
		this.noOfCopies = noOfCopies;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	@Override
	public String toString() {
		return "BookCopies [bookId=" + bookId + ", branchId=" + branchId + ", noOfCopies=" + noOfCopies + "]\n";
	}
}