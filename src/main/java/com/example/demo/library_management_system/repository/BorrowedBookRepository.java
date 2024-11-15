package com.example.demo.library_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.library_management_system.entity.BorrowedBook;



public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
	// List<BorrowedBook> findByUser(User user);
	 
	 @Query("SELECT b FROM BorrowedBook b WHERE b.user.id = :userId")
	    List<BorrowedBook> findByUserId(@Param("userId") String userId);

	  
	  Optional<BorrowedBook> findByUser_UserIdAndBook_BookId(String userId, String bookId);
	  
//	  List<BorrowedBook> findByUserId(Long userId);
}
