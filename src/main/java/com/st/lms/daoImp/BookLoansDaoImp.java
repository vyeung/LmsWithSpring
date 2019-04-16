package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.BookLoans;
import com.st.lms.models.BookLoansPrimaryKey;

@Repository
public interface BookLoansDaoImp extends JpaRepository<BookLoans, BookLoansPrimaryKey> {
	
}