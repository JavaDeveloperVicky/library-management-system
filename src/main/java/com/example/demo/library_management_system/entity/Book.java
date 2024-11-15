package com.example.demo.library_management_system.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Data

public class Book {

	@Id
	private String bookId;
	private String title;
	private String author;
	private String genre;
	private int availableCopies;

	@Enumerated(EnumType.STRING)
	private BookStatus status;

	public enum BookStatus {
		AVAILABLE, BORROWED, OVERDUE
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public List<BorrowedBook> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	public Book(String bookId,String title,String author,String genre,int availableCopies,BookStatus status) {
        this.bookId=bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availableCopies = availableCopies;
        this.status = status;
    }
	
	 // No-argument constructor
    public Book() {
    }
	
	 @OneToMany(mappedBy = "book")
	    private List<BorrowedBook> borrowedBooks;

	


}
