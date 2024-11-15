package com.example.demo.library_management_system.controller;

import com.example.demo.library_management_system.entity.Book;
import com.example.demo.library_management_system.exception.UserNotFoundException;
import com.example.demo.library_management_system.service.BookService;
import com.example.demo.library_management_system.jwt.JwtService;
import com.example.demo.library_management_system.repository.BookRepository;
import com.example.demo.library_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testSearchBook_Found() throws Exception {
        // Arrange: Creating a sample book
        Book book = new Book();
        book.setBookId("1");
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Fiction");

        // Setting up the mock behavior for the service method
        when(bookService.getbook(anyString(), anyString(), anyString())).thenReturn(List.of(book));

        // Act: Performing the GET request to search for a book
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search")
                .param("title", "Test Book")
                .param("author", "Test Author")
                .param("genre", "Fiction"))
                
                // Assert: Verifying the status and JSON response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Test Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value("Fiction"));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).getbook(anyString(), anyString(), anyString());
    }

    @Test
    public void testSearchBook_NotFound() throws Exception {
        // Arrange: No books found with the given criteria
        when(bookService.getbook(anyString(), anyString(), anyString())).thenReturn(Collections.emptyList());

        // Act: Performing the GET request with non-existent book details
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search")
                .param("title", "Nonexistent Book")
                .param("author", "Nonexistent Author")
                .param("genre", "Nonexistent Genre"))
                
                // Assert: Expecting a 404 (Not Found) status
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).getbook(anyString(), anyString(), anyString());
    }

    @Test
    public void testBorrowBook_Success() throws Exception {
        // Arrange: Prepare mock data for the borrow action
        String token = "Bearer valid-token";
        String bookId = "1";
        String dueDate = "2024-12-01";
        when(jwtService.extractUsername(anyString())).thenReturn("testUser");
        when(bookService.borrowBook(eq(bookId), eq("testUser"))).thenReturn(dueDate);

        // Act: Perform the PUT request to borrow a book
        mockMvc.perform(MockMvcRequestBuilders.put("/api/borrow")
                .header("Authorization", token)
                .param("bookId", bookId))
                
                // Assert: Verifying success response with due date
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Book borrowed successfully. Due date: 2024-12-01"));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).borrowBook(eq(bookId), eq("testUser"));
    }

    @Test
    public void testBorrowBook_Failure() throws Exception {
        // Arrange: Prepare mock data for a failed borrow action
        String token = "Bearer valid-token";
        String bookId = "1";
        when(jwtService.extractUsername(anyString())).thenReturn("testUser");
        when(bookService.borrowBook(eq(bookId), eq("testUser"))).thenReturn(null);

        // Act: Perform the PUT request to borrow a book
        mockMvc.perform(MockMvcRequestBuilders.put("/api/borrow")
                .header("Authorization", token)
                .param("bookId", bookId))
                
                // Assert: Expecting failure with a 400 (Bad Request) response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Book not available."));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).borrowBook(eq(bookId), eq("testUser"));
    }

    @Test
    public void testGetBorrowingHistory_Success() throws Exception {
        // Arrange: Prepare mock data for borrowing history
        String userId = "testUser";
        when(bookService.getBorrowingHistory(eq(userId)))
            .thenReturn(Collections.singletonList(Collections.singletonMap("bookId", "1")));

        // Act: Perform the GET request to retrieve borrowing history
        mockMvc.perform(MockMvcRequestBuilders.get("/api/{userId}/borrowing-history", userId))
                
                // Assert: Expecting a 200 OK response with borrowing history
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bookId").value("1"));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).getBorrowingHistory(eq(userId));
    }

    @Test
    public void testGetBorrowingHistory_UserNotFound() throws Exception {
        // Arrange: Simulate a scenario where the user is not found
        String userId = "nonexistentUser";
        when(bookService.getBorrowingHistory(eq(userId)))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act: Perform the GET request to retrieve borrowing history
        mockMvc.perform(MockMvcRequestBuilders.get("/api/{userId}/borrowing-history", userId))
                
                // Assert: Expecting a 404 Not Found response
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("[]"));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).getBorrowingHistory(eq(userId));
    }

    @Test
    public void testReturnBook_Success() throws Exception {
        // Arrange: Prepare mock data for the return book action
        String bookId = "1";
        String userId = "testUser";
        String returnMessage = "Book returned successfully.";
        when(bookService.returnBook(eq(userId), eq(bookId))).thenReturn(returnMessage);

        // Act: Perform the PUT request to return a book
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return")
                .param("bookId", bookId)
                .param("userId", userId))
                
                // Assert: Verifying success response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(returnMessage));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).returnBook(eq(userId), eq(bookId));
    }

    @Test
    public void testReturnBook_Failure() throws Exception {
        // Arrange: Prepare mock data for a failed return action
        String bookId = "1";
        String userId = "testUser";
        when(bookService.returnBook(eq(userId), eq(bookId))).thenThrow(new RuntimeException("Book not borrowed"));

        // Act: Perform the PUT request to return a book
        mockMvc.perform(MockMvcRequestBuilders.put("/api/return")
                .param("bookId", bookId)
                .param("userId", userId))
                
                // Assert: Expecting failure with a 400 (Bad Request) response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Book not borrowed"));

        // Verify: Ensure the service method was called once
        verify(bookService, times(1)).returnBook(eq(userId), eq(bookId));
    }
}
