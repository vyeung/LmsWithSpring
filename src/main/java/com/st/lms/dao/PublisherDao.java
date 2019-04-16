package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.Publisher;

@Repository
public interface PublisherDao extends JpaRepository<Publisher, Integer> {
	
}