package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.BookCopies;
import com.st.lms.models.BookCopiesPrimaryKey;

@Repository
public interface BookCopiesDao extends JpaRepository<BookCopies, BookCopiesPrimaryKey> {
	
}