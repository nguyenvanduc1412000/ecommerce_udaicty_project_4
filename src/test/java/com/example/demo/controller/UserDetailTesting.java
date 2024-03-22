package com.example.demo.controller;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailTesting {

    private final UserRepository userRepo = mock(UserRepository.class);
    private UserDetailsServiceImpl userDetailTesting;
    private  String username =null;
    private  String password =null;
    @Before
    public void setUp() {
        userDetailTesting = new UserDetailsServiceImpl(userRepo);
        TestUtils.injectObjects(userDetailTesting, "userRepository", userRepo);
        username ="ducnv";
        password ="123456789";
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User(username,password);
        user.setId(1L);
        when(userRepo.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetailTesting.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(password, userDetails.getPassword());
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadFailPassword() {
        User user = new User(username,password);
        user.setId(2L);
        when(userRepo.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetailTesting.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertNotEquals("12222222", userDetails.getPassword());
        assertEquals(username, userDetails.getUsername());
    }
}
