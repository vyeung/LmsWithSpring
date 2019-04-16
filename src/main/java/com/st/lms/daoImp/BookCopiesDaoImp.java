package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.BookCopies;
import com.st.lms.models.BookCopiesPrimaryKey;

@Repository
public interface BookCopiesDaoImp extends JpaRepository<BookCopies, BookCopiesPrimaryKey> {
	
}