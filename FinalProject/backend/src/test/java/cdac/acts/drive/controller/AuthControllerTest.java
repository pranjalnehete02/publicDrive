package cdac.acts.drive.controller;

import cdac.acts.drive.entity.AuthResponse;
import cdac.acts.drive.entity.LoginRequest;
import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.FolderRepository;
import cdac.acts.drive.repository.UserRepository;
import cdac.acts.drive.filter.CustomJWTTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private CustomJWTTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        authController.storagePath = "test_storage";
        authController.init(); // Initialize rootLocation

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("testPass");
        testUser.setRole("ROLE_USER");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");
    }

    @Test
    void testLogin_Success() {
        Authentication authMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(authMock.getName()).thenReturn(testUser.getUsername());
        when(tokenService.generateToken(testUser.getUsername())).thenReturn("mockToken");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AuthResponse);
        assertEquals("mockToken", ((AuthResponse) response.getBody()).getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(testUser.getUsername());
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");

        ResponseEntity<String> response = authController.registerUser(testUser);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User registered successfully, folder created, and root folder added.", response.getBody());

        verify(userRepository).save(any(User.class));
        verify(folderRepository).save(any());
    }

    @Test
    void testRegisterUser_FolderCreationFailure() {
        // Simulate failure in folder creation
        Path userFolderPath = Paths.get(authController.storagePath).resolve(String.valueOf(testUser.getId()));
        File userFolder = new File(userFolderPath.toString());
        userFolder.setWritable(false, false); // Make it unwritable

        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");

        ResponseEntity<String> response = authController.registerUser(testUser);

        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to create user folder.", response.getBody());
    }

    @Test
    void testLogout_Success() {
        authController.activeTokens = new ConcurrentHashMap<>();
        authController.activeTokens.put(testUser.getUsername(), "mockToken");

        ResponseEntity<?> response = authController.logout(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully.", response.getBody());

        assertFalse(authController.activeTokens.containsKey(testUser.getUsername()));
    }
}

