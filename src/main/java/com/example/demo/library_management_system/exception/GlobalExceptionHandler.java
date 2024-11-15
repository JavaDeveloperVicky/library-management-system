package com.example.demo.library_management_system.exception;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestControllerAdvice
public class GlobalExceptionHandler {

    
	 @ExceptionHandler(BookNotFoundException.class)
	    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }

    
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<String> handleBookNotAvailableException(BookNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    
   
    
    
    @ExceptionHandler( BookAlreadyAvailableException.class)
    public ResponseEntity<String> handleBookNotAvailableException( BookAlreadyAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    

	 @ExceptionHandler(UserNotFoundException.class)
	    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }

}