package com.example.demo.library_management_system.service;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.example.demo.library_management_system.entity.AuthRequest;
import com.example.demo.library_management_system.entity.User;
import com.example.demo.library_management_system.jwt.JwtService;
import com.example.demo.library_management_system.repository.UserRepository;
 
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
 
    @Mock
    private UserRepository userRepository;
 
    @Mock
    private JwtService jwtTokenUtil;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @InjectMocks
    private UserService userService;
 
    private User user;
 
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("1");
        user.setUsername("testuser");
        user.setPassword("password");
    }
 
    @Test
    void testLogin_Success() {
        // Mock repository to return a user
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        
        // Test the login method
        User result = userService.login("testuser", "password");
 
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }
 
    @Test
    void testLogin_UserNotFound() {
        // Mock repository to return an empty optional
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
 
        // Test for user not found scenario
        assertThrows(RuntimeException.class, () -> userService.login("testuser", "password"));
    }
 
    @Test
    void testCreateUser_Success() {
        // Mock repository and encoder behavior
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
 
        // Test the createUser method
        AuthRequest result = userService.createUser(user);
 
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }
 
    @Test
    void testCreateUser_UsernameAlreadyExists() {
        // Mock repository to return true for username existence
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
 
        // Test for username already exists scenario
        assertThrows(RuntimeException.class, () -> userService.createUser(user));
 
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
