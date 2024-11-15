package com.example.demo.library_management_system.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.library_management_system.entity.Book;
import com.example.demo.library_management_system.entity.User;
import com.example.demo.library_management_system.exception.UserNotFoundException;
import com.example.demo.library_management_system.repository.BookRepository;
import com.example.demo.library_management_system.repository.UserRepository;
import com.example.demo.library_management_system.security.UserInfoUserDetails;
import com.example.demo.library_management_system.service.BookService;
import com.example.demo.library_management_system.jwt.JwtService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	BookService bookService;
	@Autowired
	BookRepository bookRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtService jwtService;

	@GetMapping("/books/search")
	public ResponseEntity<List<Book>> searchBook(@RequestParam(required = false) String titile,
			@RequestParam(required = false) String author,
			@RequestParam(required = false) String genre) {

		List<Book> book = bookService.getbook(titile,author,genre);
		if (book.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(book, HttpStatus.FOUND);

	}

	@PutMapping("/borrow")
	public ResponseEntity<?> borrowBook(@RequestHeader("Authorization") String token,
			@RequestParam("bookId") String bookId) {

		String username=null;
		
		if (token != null && token.startsWith("Bearer ")) {

			token = token.substring(7);

			 username = jwtService.extractUsername(token);

		}
		
			String dueDate = bookService.borrowBook(bookId, username);

			if (dueDate != null) {
				return ResponseEntity.ok("Book borrowed successfully. Due date: " + dueDate);

			} else {
				return ResponseEntity.badRequest().body("Book not available.");
			}

		

	}

	@GetMapping("/{userId}/borrowing-history")
	public ResponseEntity<List<Map<String, Object>>> getBorrowingHistory(@PathVariable String userId) {
		try {
			List<Map<String, Object>> borrowingHistory = bookService.getBorrowingHistory(userId);
			return ResponseEntity.ok(borrowingHistory);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		}
	}

	@PutMapping("/return")
	public ResponseEntity<String> returnBook(@RequestParam("bookId") String bookId,
			@RequestParam("userId") String userId) {
		try {

			// Return the book
			String message = bookService.returnBook(userId, bookId);
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	

}
