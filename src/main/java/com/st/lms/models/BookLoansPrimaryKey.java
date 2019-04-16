package com.st.lms.models;

import java.io.Serializable;

@SuppressWarnings({"unused", "serial"})
public class BookLoansPrimaryKey implements Serializable {
	private Integer bookId;
	private Integer branchId;
	private Integer cardNo;
	
	public BookLoansPrimaryKey(Integer bookId, Integer branchId, Integer cardNo) {
		this.bookId = bookId;
		this.branchId = branchId;
		this.cardNo = cardNo;
	}
}