package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Borrower;


@Repository
public interface BorrowerDaoImp extends JpaRepository<Borrower, Integer>{
	
}
