package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Author;

@Repository
public interface AuthorDao extends JpaRepository<Author, Integer>{
	
}