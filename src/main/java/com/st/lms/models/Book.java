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
@Table(name = "tbl_book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "authId")
	private int authorId;
	
	@Column(name = "pubId")
	private int pubId;
	
	
	public Book() {}
	
	public Book(int bookId, String title, int authorId, int pubId) {
		this.bookId = bookId;
		this.title = title;
		this.authorId = authorId;
		this.pubId = pubId;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getAuthorId() {
		return authorId;
	}
	
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	
	public int getPubId() {
		return pubId;
	}
	
	public void setPubId(int pubId) {
		this.pubId = pubId;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + ", authorId=" + authorId + ", pubId=" + pubId + "]\n";
	}
}