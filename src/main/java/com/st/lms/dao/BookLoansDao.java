package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.BookLoans;
import com.st.lms.models.BookLoansPrimaryKey;

@Repository
public interface BookLoansDao extends JpaRepository<BookLoans, BookLoansPrimaryKey> {
	
}