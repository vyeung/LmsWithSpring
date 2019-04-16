package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Book;

@Repository
public interface BookDao extends JpaRepository<Book, Integer> {
	
}