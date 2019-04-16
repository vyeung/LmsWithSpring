package com.st.lms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "tbl_library_branch")
public class LibraryBranch {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int branchId;
	@Column(name="branchName")
	private String branchName;
	@Column(name="branchAddress")
	private String branchAddress;
	
	public LibraryBranch() {}
	
	public LibraryBranch(int branchId, String branchName, String branchAddress) {
		this.branchId = branchId;
		this.branchName = branchName;
		this.branchAddress = branchAddress;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	@Override
	public String toString() {
		return "LibraryBranch [branchId=" + branchId + ", branchName=" + branchName + ", branchAddress=" + branchAddress
				+ "]\n";
	}
}