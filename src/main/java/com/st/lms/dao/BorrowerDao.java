package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Borrower;

@Repository
public interface BorrowerDao extends JpaRepository<Borrower, Integer>{
	
}