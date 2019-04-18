package com.st.lms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.LibraryBranch;

@Repository
public interface LibBranchDao extends JpaRepository<LibraryBranch, Integer>{
	
}