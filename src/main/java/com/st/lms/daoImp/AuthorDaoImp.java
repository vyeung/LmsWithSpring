package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Author;

@Repository
public interface AuthorDaoImp extends JpaRepository<Author, Integer>{
	
}

