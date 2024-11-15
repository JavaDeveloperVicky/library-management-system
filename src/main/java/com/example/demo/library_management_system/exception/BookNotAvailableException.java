package com.example.demo.library_management_system.exception;

public class BookNotAvailableException extends RuntimeException {
	public BookNotAvailableException(String message) {
		super(message);
	}
}
