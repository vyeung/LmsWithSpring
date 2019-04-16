package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Publisher;

@Repository
public interface PublisherDaoImp extends JpaRepository<Publisher, Integer> {
	
}