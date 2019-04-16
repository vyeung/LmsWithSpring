package com.st.lms.daoImp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.st.lms.models.LibraryBranch;

@Repository
public interface LibBranchDaoImp extends JpaRepository<LibraryBranch, Integer>{
	
}
