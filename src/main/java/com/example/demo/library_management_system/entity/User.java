package com.example.demo.library_management_system.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
	 	@Id
	    private String userId;
	    private String username;
	    private String password;
	  //  private String roles;
	    
	    //No-Argument Constructor
	    public User() {
	    	
	    }
	    
	    public User(String userId, String username, String password) {
			super();
			this.userId = userId;
			this.username = username;
			this.password = password;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public List<BorrowedBook> getBorrowedBooks() {
			return borrowedBooks;
		}

		public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
			this.borrowedBooks = borrowedBooks;
		}

		@OneToMany(mappedBy = "user")
	    private List<BorrowedBook> borrowedBooks;
	/*	public List<GrantedAuthority> getRoles() {
			// TODO Auto-generated method stub
			return null;
		}
*/
}
