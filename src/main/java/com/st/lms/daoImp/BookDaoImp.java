package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Book;

@Repository
public interface BookDaoImp extends JpaRepository<Book, Integer> {
	
}