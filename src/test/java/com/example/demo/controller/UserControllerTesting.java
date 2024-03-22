package com.example.demo.controller;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTesting {

    String USERNAME = null;
    String PASSWORD = null;
    String REPASSWORD = null;
    UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        USERNAME = "ducnv";
        PASSWORD = "12345678";
        REPASSWORD = "12345678";
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserSuccess() {
        when(encoder.encode(PASSWORD)).thenReturn("hashed");
        CreateUserRequest r = new CreateUserRequest(USERNAME,PASSWORD,REPASSWORD);
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("hashed", u.getPassword());
    }

    @Test
    public void findIdFail() {
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findById() {
        CreateUserRequest r = new CreateUserRequest(USERNAME,PASSWORD,REPASSWORD);
        ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(user));

        ResponseEntity<User> userResponseEntity = userController.findById(0L);

        User u = userResponseEntity.getBody();
        assertNotNull(u);
    }

    @Test
    public void findByUserName() {
        CreateUserRequest r = new CreateUserRequest(USERNAME,PASSWORD,REPASSWORD);
        ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findByUsername(USERNAME)).thenReturn(user);

        ResponseEntity<User> userResponseEntity = userController.findByUserName(USERNAME);

        User u = userResponseEntity.getBody();
        assertNotNull(u);
    }
}
